package tourGuide.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import tourGuide.TripDealsDto;
import tripPricer.Provider;

import java.util.List;

@FeignClient(value = "TripPricerClient", url = "http://localhost:8081/")
public interface TripPricerClient {

    @RequestMapping(value = "/getTripDeals")
    public List<Provider> getProdivers(@RequestBody TripDealsDto tripDealsDto);
}
