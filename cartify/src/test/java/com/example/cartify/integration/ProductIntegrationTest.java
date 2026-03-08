package com.example.cartify.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cartify.Entity.Product;
import com.example.cartify.Repository.ProductRepository;
import com.example.cartify.config.TestDataFactory;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ProductIntegrationTest {

    @Autowired
    private ProductRepository productRepo;

    @Test
    void shouldCreateProduct() {
        Product product = TestDataFactory.createProduct();
        Product saved = productRepo.save(product);

        assertNotNull(saved.getId());
        assertEquals(50000, saved.getPrice());
    }
}