package com.example.cartify.config;

import com.example.cartify.Entity.*;

public class TestDataFactory {

    public static User createUser() {
        User u = new User();
        u.setName("Test User");
        u.setEmail("test@mail.com");
        u.setPassword("password");
        return u;
    }

    public static Product createProduct() {
        Product p = new Product();
        p.setName("Laptop");
        p.setDescription("Gaming laptop");
        p.setPrice(50000);
        p.setStock(10);
        return p;
    }
}