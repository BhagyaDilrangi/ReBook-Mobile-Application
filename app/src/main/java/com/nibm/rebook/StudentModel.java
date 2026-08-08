package com.nibm.rebook;

public class StudentModel {

    String id;
    String name;
    String email;
    String status;

    // Required for Firebase
    public StudentModel() {
    }

    public StudentModel(String id, String name, String email, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setStatus(String status) {
        this.status = status;
    }
}