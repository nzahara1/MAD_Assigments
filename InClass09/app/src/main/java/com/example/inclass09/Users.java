package com.example.inclass09;

public class Users {

    private String id;

    private String fname;

    private String lname;

    public Users(String id, String fname, String lname) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
    }

    public String getId() {
        return id;
    }

    public String getfName() {
        return fname;
    }

    public String getlName() {
        return lname;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", fName='" + fname + '\'' +
                ", lName='" + lname + '\'' +
                '}';
    }
}
