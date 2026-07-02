package com.nibm.rebook;

public class FeedbackModel {

    String user;
    String comment;
    String rating;
    String status;

    public FeedbackModel(String user, String comment, String rating, String status) {
        this.user = user;
        this.comment = comment;
        this.rating = rating;
        this.status = status;
    }

    public String getUser() { return user; }
    public String getComment() { return comment; }
    public String getRating() { return rating; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}