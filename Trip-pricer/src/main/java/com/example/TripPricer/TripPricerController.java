package com.example.TripPricer;

import com.example.TripPricer.model.TripDealModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;

@RestController
public class TripPricerController {

    private TripPricer tripPricer = new TripPricer();

    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestBody TripDealModel tripDealModel){
        List<Provider> providers = tripPricer.getPrice(tripDealModel.getApiKey(), tripDealModel.getAttractionId(),
                tripDealModel.getAdults(), tripDealModel.getChildren(), tripDealModel.getNightsStay(), tripDealModel.getRewardsPoints());
        return providers;
    }
}
