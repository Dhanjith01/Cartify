package com.example.cartify.Service;

import com.example.cartify.DTO.CartItemDTO;
import com.example.cartify.DTO.CartResponseDTO;

public interface CartService {

    CartResponseDTO addItem(Long userId, CartItemDTO item);
    CartResponseDTO removeItem(Long userId, Long productId);
    CartResponseDTO updateQuantity(Long userId, Long productId, int quantity);
    CartResponseDTO getCart(Long userId);
}