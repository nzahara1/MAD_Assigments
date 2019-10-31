package com.example.inclass09;

import com.google.gson.annotations.SerializedName;

public class Login {

    private String email;

    private String password;

    private String token;

    private String status;

    private String message;

    @SerializedName("user_fname")
    private String firstName;

    @SerializedName("user_lname")
    private String lastName;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Login(String email, String password, String token, String status, String message) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.status = status;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
