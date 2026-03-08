package com.example.cartify.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cartify.DTO.CartItemDTO;
import com.example.cartify.DTO.CartResponseDTO;
import com.example.cartify.Entity.Product;
import com.example.cartify.Entity.User;
import com.example.cartify.Repository.ProductRepository;
import com.example.cartify.Repository.UserRepository;
import com.example.cartify.Service.CartService;
import com.example.cartify.config.TestDataFactory;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class CartIntegrationTest {

    @Autowired private CartService cartService;
    @Autowired private UserRepository userRepo;
    @Autowired private ProductRepository productRepo;

    @Test
    void shouldAddItemToCart() {

        User user = userRepo.save(TestDataFactory.createUser());
        Product product = productRepo.save(TestDataFactory.createProduct());

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(product.getId());
        dto.setQuantity(2);

        CartResponseDTO cart = cartService.addItem(user.getId(), dto);

        assertEquals(1, cart.getItems().size());
        assertEquals(100000, cart.getTotalPrice());
    }
}
