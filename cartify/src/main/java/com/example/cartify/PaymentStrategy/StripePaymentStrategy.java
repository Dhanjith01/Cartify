package com.example.cartify.PaymentStrategy;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.cartify.Entity.Order;
import com.example.cartify.Entity.Payment;
import com.example.cartify.Repository.PaymentRepository;

@Service("STRIPE")
public class StripePaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepo;

    public StripePaymentStrategy(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @Override
    public Payment processPayment(Order order) {

        // Simulate Stripe API call
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalPrice());
        payment.setTransactionId("STRIPE-" + UUID.randomUUID());
        payment.setStatus("PAID");

        return paymentRepo.save(payment);
    }
}
