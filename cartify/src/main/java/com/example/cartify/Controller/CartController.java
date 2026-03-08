package com.example.cartify.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cartify.DTO.CartItemDTO;
import com.example.cartify.DTO.CartResponseDTO;

import com.example.cartify.Security.CustomUserDetails;
import com.example.cartify.Service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public CartResponseDTO addItem(@AuthenticationPrincipal CustomUserDetails user,
                                   @RequestBody CartItemDTO dto) {
        return service.addItem(user.getId(), dto);
    }

    @DeleteMapping("/remove/{productId}")
    public CartResponseDTO removeItem(@AuthenticationPrincipal CustomUserDetails user,
                                      @PathVariable Long productId) {
        return service.removeItem(user.getId(), productId);
    }

    @PutMapping("/update/{productId}/{quantity}")
    public CartResponseDTO updateItem(@AuthenticationPrincipal CustomUserDetails user,
                                      @PathVariable Long productId,
                                      @PathVariable int quantity) {
        return service.updateQuantity(user.getId(), productId, quantity);
    }

    @GetMapping
    public CartResponseDTO getCart(@AuthenticationPrincipal CustomUserDetails user) {
        return service.getCart(user.getId());
    }
}
