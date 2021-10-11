package tourGuide.httpClients;

import tourGuide.domain.gpsUtil.Attraction;
import tourGuide.domain.gpsUtil.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilClient {

    private RestTemplate restTemplate = new RestTemplate();

    private String uri = "http://localhost:8083";

    public VisitedLocation getUserLocation(UUID userId){
        ResponseEntity<VisitedLocation> response = restTemplate.exchange(
                uri+"/getUserLocation/"+userId.toString(), HttpMethod.GET, null, VisitedLocation.class);
        return response.getBody();
    };

    public List<Attraction> getAttractions(){
        ResponseEntity<List<Attraction>> response = restTemplate.exchange(
                uri+"/getAttractions", HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>(){});
        return response.getBody();
    }
}
