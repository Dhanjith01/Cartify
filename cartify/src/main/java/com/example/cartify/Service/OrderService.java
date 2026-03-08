package com.example.cartify.Service;

import java.util.List;


import com.example.cartify.DTO.OrderResponseDTO;


public interface OrderService {

    OrderResponseDTO checkout(Long userId);
    List<OrderResponseDTO> getUserOrders(Long userId);
    List<OrderResponseDTO> getAllOrders(); // admin
}