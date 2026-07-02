package com.nibm.rebook;

public class MaterialModel {

    String title;
    String type;
    String status;

    public MaterialModel(String title, String type, String status) {
        this.title = title;
        this.type = type;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}