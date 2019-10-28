package com.example.homework06;

public class Profile {

    private String firstName;

    private String lastName;

    private String studentId;

    private String department;

    private int imageDrawable;

    public Profile(String firstName, String lastName, String studentId, String department, int imageDrawable) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.department = department;
        this.imageDrawable = imageDrawable;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDepartment() {
        return department;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", department='" + department + '\'' +
                ", imageDrawable=" + imageDrawable +
                '}';
    }
}
