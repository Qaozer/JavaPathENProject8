package com.rewardCentral;

import com.rewardCentral.model.AttractionRewardPointsDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rewardCentral.RewardCentral;

@RestController
public class RewardCentralController {

    private final RewardCentral rewardCentral = new RewardCentral();

    @GetMapping("/getAttractionRewardPoints")
    public int getAttractionRewardPoints(AttractionRewardPointsDto rewardDto){
        return rewardCentral.getAttractionRewardPoints(rewardDto.getAttractionId(), rewardDto.getUserId());
    }
}
