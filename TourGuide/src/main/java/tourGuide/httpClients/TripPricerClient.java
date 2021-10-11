package tourGuide.httpClients;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tourGuide.TripDealsDto;
import tripPricer.Provider;

import java.util.List;

@Service
public class TripPricerClient {

    private RestTemplate restTemplate = new RestTemplate();

    private String uri = "http://localhost:8081";

    public List<Provider> getProviders(TripDealsDto tripDealsDto){
        HttpEntity<TripDealsDto> body = new HttpEntity<>(tripDealsDto);
        ResponseEntity<List<Provider>> response = restTemplate.exchange(
                uri+"/getTripDeals", HttpMethod.GET, body, new ParameterizedTypeReference<List<Provider>>(){});
        return response.getBody();
    }
}
