package com.nibm.rebook;

public class ReportModel {

    String title;
    String description;
    String status;

    // Required for Firebase
    public ReportModel() {
    }

    public ReportModel(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) {
        this.status = status;
    }
}