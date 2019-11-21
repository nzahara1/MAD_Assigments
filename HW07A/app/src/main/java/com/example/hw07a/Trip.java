package com.example.hw07a;

import java.io.Serializable;

public class Trip implements Serializable {

    public String imageUrl;

    public String name;

    public String lat;

    public String lon;

    public String chatName;

    public String userId;

    public Trip(String imageUrl, String name, String lat, String lon, String chatName, String userId) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.chatName = chatName;
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getChatName() {
        return chatName;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "{" +
                "imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", chatName='" + chatName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
