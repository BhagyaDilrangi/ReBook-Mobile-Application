package com.nibm.rebook;

/**
 * Model class for chat messages used across the application.
 */
public class ChatMessage {
    private String message;
    private String senderId;
    private String receiverId;
    private long timestamp;
    private boolean isSentByUser; // Field for UI-based chat models

    // Required for Firebase
    public ChatMessage() {
    }

    // Constructor for Firebase/Data usage
    public ChatMessage(String message, String senderId, String receiverId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    // Constructor for UI/Local usage (e.g. ChatActivity)
    public ChatMessage(String message, boolean isSentByUser) {
        this.message = message;
        this.isSentByUser = isSentByUser;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isSentByUser() { return isSentByUser; }
    public void setSentByUser(boolean sentByUser) { isSentByUser = sentByUser; }
}
