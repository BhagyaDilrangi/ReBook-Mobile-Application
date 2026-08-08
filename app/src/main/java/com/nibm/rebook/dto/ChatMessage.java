package com.nibm.rebook.dto;

public class ChatMessage {
    private String message;
    private boolean isSentByUser; // true if I sent it, false if I received it

    public ChatMessage(String message, boolean isSentByUser) {
        this.message = message;
        this.isSentByUser = isSentByUser;
    }

    public String getMessage() { return message; }
    public boolean isSentByUser() { return isSentByUser; }
}