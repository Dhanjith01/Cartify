package com.example.cartify.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cartify.Entity.User;
import com.example.cartify.Repository.UserRepository;
import com.example.cartify.config.TestDataFactory;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void shouldSaveUser() {
        User user = TestDataFactory.createUser();
        User saved = userRepo.save(user);

        assertNotNull(saved.getId());
        assertEquals("Test User", saved.getName());
    }
}