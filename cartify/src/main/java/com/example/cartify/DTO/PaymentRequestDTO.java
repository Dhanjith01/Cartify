package com.example.cartify.DTO;

public class PaymentRequestDTO {
    private Long orderId;
    private String paymentMode; // STRIPE, UPI, WALLET
    
    // Getters

    public Long getOrderId() {
        return orderId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    // Setters

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}