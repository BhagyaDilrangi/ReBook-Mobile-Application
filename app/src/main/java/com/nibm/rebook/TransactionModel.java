package com.nibm.rebook;

public class TransactionModel {

    String id;
    String customer;
    String amount;
    String status;

    // Required for Firebase
    public TransactionModel() {
    }

    public TransactionModel(String id, String customer, String amount, String status) {
        this.id = id;
        this.customer = customer;
        this.amount = amount;
        this.status = status;
    }

    public String getId() { return id; }
    public String getCustomer() { return customer; }
    public String getAmount() { return amount; }
    public String getStatus() { return status; }

    public void setId(String id) { this.id = id; }
    public void setCustomer(String customer) { this.customer = customer; }
    public void setAmount(String amount) { this.amount = amount; }
    public void setStatus(String status) {
        this.status = status;
    }
}