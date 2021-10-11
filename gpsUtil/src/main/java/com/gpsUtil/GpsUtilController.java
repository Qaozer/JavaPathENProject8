package com.gpsUtil;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsUtilController {

    private final GpsUtil gpsUtil = new GpsUtil();

    @GetMapping(value = "/getUserLocation/{userId}")
    public VisitedLocation getUserLocation(@PathVariable UUID userId){
        return gpsUtil.getUserLocation(userId);
    }

    @GetMapping(value = "/getAttractions")
    public List<Attraction> getAttractions(){
        return gpsUtil.getAttractions();
    }
}
