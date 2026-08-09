package com.nibm.rebook.dto;

/**
 * Model class for chat messages used across the application.
 */
public class ChatMessage {
    private String message;
    private String senderId;
    private String receiverId;
    private long timestamp;
    private boolean isSentByUser; // Field for UI-based chat models

    // Required empty constructor for Firebase Realtime Database deserialization
    public ChatMessage() {
    }

    // Constructor for Firebase / Database data usage
    public ChatMessage(String message, String senderId, String receiverId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    // Constructor for local UI usage
    public ChatMessage(String message, boolean isSentByUser) {
        this.message = message;
        this.isSentByUser = isSentByUser;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }
    public void setSentByUser(boolean sentByUser) {
        this.isSentByUser = sentByUser;
    }
}