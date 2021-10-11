package com.example.TripPricer.model;

import java.util.UUID;

public class TripDealModel {
    private String apiKey;
    private UUID attractionId;
    private int adults;
    private int children;
    private int nightsStay;
    private int rewardsPoints;

    public TripDealModel() {
    }

    public TripDealModel(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        this.apiKey = apiKey;
        this.attractionId = attractionId;
        this.adults = adults;
        this.children = children;
        this.nightsStay = nightsStay;
        this.rewardsPoints = rewardsPoints;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getNightsStay() {
        return nightsStay;
    }

    public void setNightsStay(int nightsStay) {
        this.nightsStay = nightsStay;
    }

    public int getRewardsPoints() {
        return rewardsPoints;
    }

    public void setRewardsPoints(int rewardsPoints) {
        this.rewardsPoints = rewardsPoints;
    }
}
