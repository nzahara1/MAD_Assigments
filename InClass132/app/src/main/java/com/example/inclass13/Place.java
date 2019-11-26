package com.example.inclass13;

public class Place {

    private String description;

    private String placeId;

    public Place(String description, String placeId) {
        this.description = description;
        this.placeId = placeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "Place{" +
                "description='" + description + '\'' +
                ", placeId='" + placeId + '\'' +
                '}';
    }
}
