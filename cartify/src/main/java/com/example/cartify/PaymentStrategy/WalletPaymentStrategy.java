package com.example.cartify.PaymentStrategy;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.cartify.Entity.Order;
import com.example.cartify.Entity.Payment;
import com.example.cartify.Repository.PaymentRepository;

@Service("WALLET")
public class WalletPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepo;

    public WalletPaymentStrategy(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @Override
    public Payment processPayment(Order order) {

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalPrice());
        payment.setTransactionId("WALLET-" + UUID.randomUUID());
        payment.setStatus("PAID");

        return paymentRepo.save(payment);
    }
}