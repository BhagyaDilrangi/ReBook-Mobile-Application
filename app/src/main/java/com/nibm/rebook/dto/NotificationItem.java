package com.nibm.rebook.dto;

public class NotificationItem {
    private String title;
    private String message;
    private long timestamp;

    // Required for Firebase
    public NotificationItem() {
    }

    public NotificationItem(String title, String message) {
        this.title = title;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}