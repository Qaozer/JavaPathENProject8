package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.domain.gpsUtil.Attraction;
import tourGuide.domain.gpsUtil.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.httpClients.GpsUtilClient;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {tourGuide.httpClients.GpsUtilClient.class, tourGuide.httpClients.RewardCentralClient.class, tourGuide.httpClients.TripPricerClient.class})
public class TestRewardsService {


	@Before
	public void setup(){
		Locale.setDefault(Locale.US);
	}

	@Test
	public void userGetRewards() {
		GpsUtilClient gpsUtilClient = new GpsUtilClient();
		RewardsService rewardsService = new RewardsService();

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilClient.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtilClient gpsUtilClient = new GpsUtilClient();
		RewardsService rewardsService = new RewardsService();
		Attraction attraction = gpsUtilClient.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		GpsUtilClient gpsUtilClient = new GpsUtilClient();
		RewardsService rewardsService = new RewardsService();
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(rewardsService);
		
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtilClient.getAttractions().size(), userRewards.size());
	}
	
}
