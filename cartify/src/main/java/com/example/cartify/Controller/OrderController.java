package com.example.cartify.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cartify.DTO.OrderResponseDTO;
import com.example.cartify.Security.CustomUserDetails;
import com.example.cartify.Service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/checkout")
    public OrderResponseDTO checkout(@AuthenticationPrincipal CustomUserDetails user) {
        return service.checkout(user.getId());
    }

    @GetMapping("/my")
    public List<OrderResponseDTO> getUserOrders(@AuthenticationPrincipal CustomUserDetails user) {
        return service.getUserOrders(user.getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponseDTO> getAllOrders() {
        return service.getAllOrders();
    }
}
