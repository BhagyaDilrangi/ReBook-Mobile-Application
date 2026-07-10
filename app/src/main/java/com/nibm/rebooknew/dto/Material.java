package com.nibm.rebooknew.dto;

public class Material {
    private String title;
    private String status;

    public Material(String title, String status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}