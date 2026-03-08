package com.example.cartify.DTO;


import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private List<CartItemResponseDTO> items;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;

    // Getters

    public Long getId() {
        return id;
    }

    public List<CartItemResponseDTO> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setItems(List<CartItemResponseDTO> items) {
        this.items = items;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
