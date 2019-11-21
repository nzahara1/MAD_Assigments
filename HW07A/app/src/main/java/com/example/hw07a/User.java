package com.example.hw07a;

import java.io.Serializable;

public class User implements Serializable {

    public String firstName;

    public String lastName;

    public String userName;

    public String password;

    public String gender;
    public String avatar_url;

    private String chatRooms;

    public User(String firstName, String lastName, String userName, String password, String gender, String avatar_url) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.avatar_url = avatar_url;
    }

    public User() {}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getChatRooms() {
        return chatRooms;
    }

    @Override
    public String toString() {
        return "{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", chatRooms='" + chatRooms + '\'' +
                '}';
    }
}
