package com.example.cartify.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.example.cartify.DTO.CartItemDTO;
import com.example.cartify.DTO.OrderResponseDTO;
import com.example.cartify.Entity.Product;
import com.example.cartify.Entity.User;
import com.example.cartify.Repository.ProductRepository;
import com.example.cartify.Repository.UserRepository;
import com.example.cartify.Service.CartService;
import com.example.cartify.Service.OrderService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;

    // ==============================
    // TEST 1 — Checkout Flow
    // ==============================
    @Test
    void shouldCheckoutSuccessfully() {

        // 1️⃣ Create user
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@mail.com");
        user.setPassword("pass");
        user = userRepo.save(user);

        // 2️⃣ Create product
        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("Gaming Laptop");
        product.setPrice(50000);
        product.setStock(10);
        product = productRepo.save(product);

        // 3️⃣ Add item to cart
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(product.getId());
        dto.setQuantity(2);

        cartService.addItem(user.getId(), dto);

        // 4️⃣ Checkout
        OrderResponseDTO order = orderService.checkout(user.getId());

        // Assertions
        assertNotNull(order.getId());
        assertEquals(100000, order.getTotalPrice());
        assertEquals(1, order.getItems().size());
    }

    // ==============================
    // TEST 2 — Cart Cleared After Checkout
    // ==============================
    @Test
    void shouldClearCartAfterCheckout() {
        User temp = new User();
        temp.setName("Test User");
        temp.setEmail("test@mail.com");
        temp.setPassword("pass");
        

        User user = userRepo.save(temp);

        Product product = new Product();
        product.setName("Phone");
        product.setPrice(20000);
        product.setStock(5);
        product = productRepo.save(product);

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(product.getId());
        dto.setQuantity(1);

        cartService.addItem(user.getId(), dto);

        orderService.checkout(user.getId());

        // Cart should now be empty
        var cart = cartService.getCart(user.getId());

        assertTrue(cart.getItems().isEmpty());
    }

    // ==============================
    // TEST 3 — Get User Orders
    // ==============================
    @Test
    void shouldReturnUserOrders() {

        User temp = new User();
        temp.setName("Test User");
        temp.setEmail("test@mail.com");
        temp.setPassword("pass");
        

        User user = userRepo.save(temp);

        Product product = new Product();
        product.setName("Tablet");
        product.setPrice(30000);
        product.setStock(3);
        product = productRepo.save(product);

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(product.getId());
        dto.setQuantity(1);

        cartService.addItem(user.getId(), dto);

        orderService.checkout(user.getId());

        List<OrderResponseDTO> orders =
                orderService.getUserOrders(user.getId());

        assertEquals(1, orders.size());
    }

    // ==============================
    // TEST 4 — Empty Cart Checkout Fail
    // ==============================
    @Test
    void shouldFailCheckoutIfCartEmpty() {

        User temp = new User();
        temp.setName("Test User");
        temp.setEmail("test@mail.com");
        temp.setPassword("pass");
        

        User user = userRepo.save(temp);

        Exception ex = assertThrows(
                RuntimeException.class,
                () -> orderService.checkout(user.getId())
        );

        assertEquals("Cart is empty", ex.getMessage());
    }
}
