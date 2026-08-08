package com.nibm.rebook.dto;

public class HistoryItem {
    private String title;
    private String date;

    // Required for Firebase
    public HistoryItem() {
    }

    public HistoryItem(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}