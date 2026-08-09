package com.nibm.rebook.dto;

public class BorrowItem {
    private String title;
    private String dueDate;
    private String status;

    // Required for Firebase
    public BorrowItem() {
    }

    public BorrowItem(String title, String dueDate, String status) {
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}