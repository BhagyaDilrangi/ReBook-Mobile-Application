package com.nibm.rebook;

public class MaterialModel {
    private String id;
    private String title;
    private String status;
    private String type;
    private String category;
    private String sellerId;
    private double price;

    // Required for Firebase
    public MaterialModel() {
    }

    public MaterialModel(String id, String title, String status, String type, String category, String sellerId, double price) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.type = type;
        this.category = category;
        this.sellerId = sellerId;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}