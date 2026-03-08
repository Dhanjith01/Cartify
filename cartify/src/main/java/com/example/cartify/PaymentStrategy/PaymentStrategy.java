package com.example.cartify.PaymentStrategy;

import com.example.cartify.Entity.Order;
import com.example.cartify.Entity.Payment;

public interface PaymentStrategy {

    Payment processPayment(Order order);
}