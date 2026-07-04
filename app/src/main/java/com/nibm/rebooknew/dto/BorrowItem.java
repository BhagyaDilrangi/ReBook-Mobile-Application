package com.nibm.rebooknew;

public class BorrowItem {
    private String title;
    private String dueDate;
    private String status;

    public BorrowItem(String title, String dueDate, String status) {
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public String getStatus() { return status; }
}