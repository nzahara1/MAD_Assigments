package com.example.inclass13;

import java.io.Serializable;

public class Trip implements Serializable {

    public String tripName;

    public String tripCity;

    public String placeId;

    public Trip(String tripName, String tripCity, String placeId) {
        this.tripName = tripName;
        this.tripCity = tripCity;
        this.placeId = placeId;
    }

    public String getTripName() {
        return tripName;
    }

    public String getTripCity() {
        return tripCity;
    }

    public String getPlaceId() {
        return placeId;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripName='" + tripName + '\'' +
                ", tripCity='" + tripCity + '\'' +
                ", placeId='" + placeId + '\'' +
                '}';
    }
}
