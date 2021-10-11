package tourGuide;

import java.util.UUID;

public class AttractionRewardPointsDto {
    private UUID attractionId;
    private UUID userId;

    public AttractionRewardPointsDto() {
    }

    public AttractionRewardPointsDto(UUID attractionId, UUID userId) {
        this.attractionId = attractionId;
        this.userId = userId;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
