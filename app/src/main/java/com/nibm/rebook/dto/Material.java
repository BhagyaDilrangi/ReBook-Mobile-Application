package com.nibm.rebook.dto;

public class Material {
    private String id;
    private String title;
    private String status;
    private String type; // Sale, Borrow, Donate
    private String category; // Books, Notes, Past Papers, Calculators
    private String sellerId;
    private String imageUrl;
    private String previewUrl;
    private double price;

    // Required for Firebase
    public Material() {
    }

    public Material(String title, String status) {
        this.title = title;
        this.status = status;
    }

    public Material(String id, String title, String status, String type, String category, String sellerId, double price) {
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

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPreviewUrl() { return previewUrl; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}