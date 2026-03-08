package com.example.cartify.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cartify.Entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {}