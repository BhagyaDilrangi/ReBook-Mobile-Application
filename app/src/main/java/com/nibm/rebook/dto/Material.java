package com.nibm.rebook.dto;

public class Material {
    private String materialId;
    private String title;
    private String status;
    private String type;
    private String category;
    private String sellerId;
    private double price;
    private String imageUrl;
    private String pdfFile;

    public Material() {
        // Required for Firebase Realtime Database
    }

    public Material(String materialId, String title, String status, String type, String category, String sellerId, double price) {
        this.materialId = materialId;
        this.title = title;
        this.status = status;
        this.type = type;
        this.category = category;
        this.sellerId = sellerId;
        this.price = price;
    }

    // Helper method matching potential legacy adapter calls
    public String getId() {
        return materialId;
    }

    public void setId(String id) {
        this.materialId = id;
    }

    // Standard Getters and Setters
    public String getMaterialId() { return materialId; }
    public void setMaterialId(String materialId) { this.materialId = materialId; }

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

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPdfFile() { return pdfFile; }
    public void setPdfFile(String pdfFile) { this.pdfFile = pdfFile; }
}