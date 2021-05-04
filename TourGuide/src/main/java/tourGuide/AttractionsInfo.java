package tourGuide;

public class AttractionsInfo {
    private String attractionName;
    private double longitude;
    private double latitude;
    private double distance;
    private int reward;

    public AttractionsInfo() {
    }

    public AttractionsInfo(String attractionName, double longitude, double latitude, double distance, int reward) {
        this.attractionName = attractionName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
        this.reward = reward;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
