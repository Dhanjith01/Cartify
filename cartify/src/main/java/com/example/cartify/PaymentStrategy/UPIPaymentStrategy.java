package com.example.cartify.PaymentStrategy;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.cartify.Entity.Order;
import com.example.cartify.Entity.Payment;
import com.example.cartify.Repository.PaymentRepository;

@Service("UPI")
public class UPIPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepo;

    public UPIPaymentStrategy(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @Override
    public Payment processPayment(Order order) {

        // Simulate UPI gateway
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalPrice());
        payment.setTransactionId("UPI-" + UUID.randomUUID());
        payment.setStatus("PAID");

        return paymentRepo.save(payment);
    }
}