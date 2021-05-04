package tourGuide;

import gpsUtil.location.Location;

import java.util.List;

public class NearbyAttractionsInfo {
    private Location userLocation;
    private List<AttractionsInfo> attractionsInfoList;

    public NearbyAttractionsInfo() {
    }

    public NearbyAttractionsInfo(Location userLocation, List<AttractionsInfo> attractionsInfoList) {
        this.userLocation = userLocation;
        this.attractionsInfoList = attractionsInfoList;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public List<AttractionsInfo> getAttractionsInfoList() {
        return attractionsInfoList;
    }

    public void setAttractionsInfoList(List<AttractionsInfo> attractionsInfoList) {
        this.attractionsInfoList = attractionsInfoList;
    }

}
