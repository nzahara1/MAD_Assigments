package com.example.hw07a;

import java.io.Serializable;

public class User implements Serializable {

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private String gender;

    private String chatRooms;

    public User(String firstName, String lastName, String userName, String password, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.gender = gender;
    }

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
