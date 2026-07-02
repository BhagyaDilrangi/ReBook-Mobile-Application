package com.nibm.rebook;

public class TransactionModel {

    String id;
    String customer;
    String amount;
    String status;

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

    public void setStatus(String status) {
        this.status = status;
    }
}