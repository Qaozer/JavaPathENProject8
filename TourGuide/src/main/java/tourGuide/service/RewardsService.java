package tourGuide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tourGuide.domain.gpsUtil.Attraction;
import tourGuide.domain.gpsUtil.Location;
import tourGuide.domain.gpsUtil.VisitedLocation;
import tourGuide.AttractionRewardPointsDto;
import tourGuide.httpClients.GpsUtilClient;
import tourGuide.httpClients.RewardCentralClient;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
public class RewardsService {

	@Autowired
	private RewardCentralClient rewardCentralClient;
	@Autowired
	private GpsUtilClient gpsUtilClient;
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final ExecutorService executorService = Executors.newFixedThreadPool(1000);

	public RewardsService() {
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	public Void calculateRewards(User user) {
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>(user.getVisitedLocations());
		CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>(gpsUtilClient.getAttractions());
		
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						AttractionRewardPointsDto rewardDto = new AttractionRewardPointsDto(attraction.attractionId, user.getUserId());
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(rewardDto)));
					}
				}
			}
		}
		return null;
	}

	public void calculateRewardsWithThread(List<User> users){
		List<Callable<Void>> tasks = new ArrayList<>();
		for (User user: users) {
			tasks.add(() -> calculateRewards(user));
		}

		try {
			List<Future<Void>> futures = executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	private int getRewardPoints(AttractionRewardPointsDto rewardDto) {
		return rewardCentralClient.getAttractionRewardPoints(rewardDto);
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
