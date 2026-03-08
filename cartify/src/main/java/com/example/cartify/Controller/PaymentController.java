package com.example.cartify.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.cartify.DTO.PaymentResponseDTO;
import com.example.cartify.Security.CustomUserDetails;
import com.example.cartify.DTO.PaymentRequestDTO;
import com.example.cartify.Service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*") // Next.js support
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public PaymentResponseDTO pay(
            @AuthenticationPrincipal
            CustomUserDetails user,
            @RequestBody PaymentRequestDTO request) {

        Long userId = user.getId();

        return paymentService.makePayment(userId, request);
    }
}
