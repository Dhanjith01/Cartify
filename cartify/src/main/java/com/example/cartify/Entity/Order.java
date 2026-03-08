package com.example.cartify.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private double totalPrice;
    private String status; // PENDING, COMPLETED, CANCELLED

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and setters

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public User getUser(){return user;}
    public void setUser(User user){this.user=user;}

    public double getTotalPrice(){return totalPrice;}
    public void setTotalPrice(double totalPrice){this.totalPrice=totalPrice;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public List<OrderItem> getItems() {return items;}
    public void setOrderItems(List<OrderItem> items) {this.items = items;}

    public LocalDateTime getCreatedAt(){return createdAt;}
}
