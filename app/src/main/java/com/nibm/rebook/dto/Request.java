package com.nibm.rebook.dto;

public class Request {
    private String id;
    private String materialId;
    private String materialTitle;
    private String buyerId;
    private String buyerName;
    private String sellerId;
    private String status;
    private String type;
    private String phone;
    private String address;
    private String duration;

    // Required for Firebase
    public Request() {
    }

    public Request(String materialTitle, String buyerName) {
        this.materialTitle = materialTitle;
        this.buyerName = buyerName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMaterialId() { return materialId; }
    public void setMaterialId(String materialId) { this.materialId = materialId; }

    public String getMaterialTitle() { return materialTitle; }
    public void setMaterialTitle(String materialTitle) { this.materialTitle = materialTitle; }

    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}