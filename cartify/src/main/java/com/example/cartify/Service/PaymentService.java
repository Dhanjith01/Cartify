package com.example.cartify.Service;


import com.example.cartify.DTO.PaymentRequestDTO;
import com.example.cartify.DTO.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO makePayment(Long userId,
                                   PaymentRequestDTO request);
}