package com.proglang.fap.demo.models;

public class PaymentRequest {

    private double amount;  // Amount to be paid (in PHP)
    private String description;  // Currency type (e.g., "PHP")

    // Default constructor
    public PaymentRequest() {}

    // Constructor with parameters
    public PaymentRequest(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    // Getters and setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String currency) {
        this.description = currency;
    }
}

