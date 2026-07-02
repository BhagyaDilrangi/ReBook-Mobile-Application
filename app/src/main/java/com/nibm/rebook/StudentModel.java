package com.nibm.rebook;

public class StudentModel {

    String name;
    String email;
    String status;

    public StudentModel(String name, String email, String status) {
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}