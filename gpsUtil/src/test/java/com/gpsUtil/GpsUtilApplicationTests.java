package com.gpsUtil;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
class GpsUtilApplicationTests {

	private GpsUtil gpsUtil = new GpsUtil();

	@Test
	void getUserLocationTest() {
		Locale.setDefault(Locale.US);
		UUID randomUUID = UUID.randomUUID();
		VisitedLocation location = gpsUtil.getUserLocation(randomUUID);
		assertTrue(location.userId.equals(randomUUID));
	}

	@Test
	void getAttractionsTest(){
		List<Attraction> attractionList = gpsUtil.getAttractions();
		assertFalse(attractionList.isEmpty());
	}
}
