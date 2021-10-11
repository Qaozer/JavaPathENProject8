package com.example.TripPricer;

import com.example.TripPricer.model.TripDealModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class TripPricerApplicationTests {

	@Autowired
	private TripPricerController tripPricerController;

	@Test
	void getTripDealsTest() {
		TripDealModel tripDealModel = createTripDealModel();
		List<Provider> providers = tripPricerController.getTripDeals(tripDealModel);
		System.out.println(providers.size());
	}

	private TripDealModel createTripDealModel(){
		TripDealModel dto = new TripDealModel();
		dto.setAdults(2);
		dto.setApiKey("apiKey");
		dto.setAttractionId(UUID.randomUUID());
		dto.setChildren(1);
		dto.setNightsStay(2);
		dto.setRewardsPoints(0);
		return dto;
	}
}
