package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.AttractionsInfo;
import tourGuide.NearbyAttractionsInfo;
import tourGuide.TripDealsDto;
import tourGuide.feignClient.TripPricerClient;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {

	@Autowired
	private TripPricerClient tripPricerClient;
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	private final ExecutorService executorService = Executors.newFixedThreadPool(1000);
	boolean testMode = true;
	private final RewardCentral rewardCentral = new RewardCentral();

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}
	
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}
	
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}
	
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}
	
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		TripDealsDto tripDealsDto = new TripDealsDto(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		List<Provider> providers = tripPricerClient.getProdivers(tripDealsDto);
		user.setTripDeals(providers);
		return providers;
	}
	
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	public List<VisitedLocation> trackUserLocationWithThread (List<User> users){
		List<Callable<VisitedLocation>> tasks = new ArrayList<>();
		List<VisitedLocation> visitedLocations = new ArrayList<>();
		for (User user: users){
			tasks.add(()-> trackUserLocation(user));
		}
		try {
			List<Future<VisitedLocation>> futures = executorService.invokeAll(tasks);
			for (Future<VisitedLocation> future : futures){
				visitedLocations.add(future.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return visitedLocations;
	}

	public Void getCurrentLocation(User user, Map<UUID,Location> map){
		map.put(user.getUserId(), user.getLastVisitedLocation().location);
		return null;
	}

	public Map<UUID, Location> getAllCurrentLocation(){
		List<User> users = getAllUsers();
		Map<UUID, Location> userLocations = new HashMap<>();

		List<Callable<Void>> tasks = new ArrayList<>();
		for (User user: users
			 ) {
			tasks.add(() -> getCurrentLocation(user, userLocations));
		}

		try {
			List<Future<Void>> futures = executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return userLocations;
	}

	public NearbyAttractionsInfo getNearByAttractions(VisitedLocation visitedLocation) {
		NearbyAttractionsInfo nearbyAttractionsInfo = new NearbyAttractionsInfo();
		nearbyAttractionsInfo.setUserLocation(visitedLocation.location);

		List<AttractionsInfo> attractionsInfoList = new ArrayList<>();

		Map<Double, Attraction> attractionsDistance = new HashMap<>();

		for(Attraction attraction : gpsUtil.getAttractions()) {
			attractionsDistance.put(rewardsService.getDistance(attraction, visitedLocation.location), attraction );
		}

		List<Map.Entry<Double, Attraction>> sorted = attractionsDistance.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());

		for (int i = 0; i < 5 && i < sorted.size(); i++) {
			Attraction attraction = sorted.get(i).getValue();
			AttractionsInfo attractionsInfo = new AttractionsInfo(attraction.attractionName,
					attraction.longitude, attraction.latitude, sorted.get(i).getKey(),
					rewardCentral.getAttractionRewardPoints(attraction.attractionId,visitedLocation.userId));
			attractionsInfoList.add(attractionsInfo);
		}

		nearbyAttractionsInfo.setAttractionsInfoList(attractionsInfoList);
		
		return nearbyAttractionsInfo;
	}
	
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	
}
