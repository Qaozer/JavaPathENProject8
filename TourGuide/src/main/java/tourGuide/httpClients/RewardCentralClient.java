package tourGuide.httpClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tourGuide.AttractionRewardPointsDto;

@Service
public class RewardCentralClient {

    private RestTemplate restTemplate = new RestTemplate();

    private String uri = "http://localhost:8082";

    public int getAttractionRewardPoints(AttractionRewardPointsDto attractionRewardPointsDto){
        HttpEntity<AttractionRewardPointsDto> body = new HttpEntity<>(attractionRewardPointsDto);
        ResponseEntity<Integer> response = restTemplate.exchange(
                uri+"/getAttractionRewardPoints", HttpMethod.GET, body, Integer.class);
        return response.getBody();
    }
}
