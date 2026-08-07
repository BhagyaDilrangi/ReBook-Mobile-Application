package com.nibm.rebook.dto;

public class Request {
    private String materialTitle, buyerName;
    public Request(String materialTitle, String buyerName) {
        this.materialTitle = materialTitle;
        this.buyerName = buyerName;
    }
    public String getMaterialTitle() { return materialTitle; }
    public String getBuyerName() { return buyerName; }
}