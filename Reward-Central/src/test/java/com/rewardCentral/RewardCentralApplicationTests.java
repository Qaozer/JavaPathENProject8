package com.rewardCentral;

import com.rewardCentral.model.AttractionRewardPointsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

@SpringBootTest
class RewardCentralApplicationTests {

	@Autowired
	private RewardCentralController rewardCentralController;

	@Test
	void getAttractionRewardPointsTest() {
		AttractionRewardPointsDto dto = createDto();
		int points = rewardCentralController.getAttractionRewardPoints(dto);

		assertTrue(points > 0);
		assertTrue(points < 1001);
	}

	private AttractionRewardPointsDto createDto(){
		AttractionRewardPointsDto dto = new AttractionRewardPointsDto();
		dto.setAttractionId(UUID.randomUUID());
		dto.setUserId(UUID.randomUUID());
		return dto;
	}

}
