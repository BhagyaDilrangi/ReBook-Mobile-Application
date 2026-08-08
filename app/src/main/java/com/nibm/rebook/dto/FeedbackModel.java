package com.nibm.rebook.dto;

public class FeedbackModel {

    String user;
    String targetSeller;
    String comment;
    String rating;
    String status;

    // Required for Firebase
    public FeedbackModel() {
    }

    public FeedbackModel(String user, String targetSeller, String comment, String rating, String status) {
        this.user = user;
        this.targetSeller = targetSeller;
        this.comment = comment;
        this.rating = rating;
        this.status = status;
    }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getTargetSeller() { return targetSeller; }
    public void setTargetSeller(String targetSeller) { this.targetSeller = targetSeller; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }
}
