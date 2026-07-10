package com.nibm.rebooknew.dto;

public class HistoryItem {
    private String title;
    private String date;

    public HistoryItem(String title, String date) {
        this.title = title;
        this.date = date;
    }
    public String getTitle() { return title; }
    public String getDate() { return date; }
}