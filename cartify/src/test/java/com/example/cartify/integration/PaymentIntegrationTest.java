package com.example.cartify.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cartify.DTO.PaymentRequestDTO;
import com.example.cartify.DTO.PaymentResponseDTO;
import com.example.cartify.Entity.Order;
import com.example.cartify.Entity.User;
import com.example.cartify.Repository.OrderRepository;
import com.example.cartify.Repository.UserRepository;
import com.example.cartify.Service.PaymentService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class PaymentIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User user;
    private Order order;

    @BeforeEach
    void setup() {

        // 🔹 Create User
        user = new User();
        user.setName("Test User");
        user.setEmail("test@mail.com");
        user.setPassword("pass123");

        user = userRepository.save(user);

        // 🔹 Create Order linked to user
        order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setTotalPrice(2000.0);

        order = orderRepository.save(order);
    }

    // ✅ 1. Successful UPI Payment
    @Test
    void shouldProcessUpiPaymentSuccessfully() {

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(order.getId());
        request.setPaymentMode("UPI");   // must match bean name

        PaymentResponseDTO response =
                paymentService.makePayment(user.getId(), request);

        assertNotNull(response);
        assertEquals(order.getId(), response.getOrderId());
        assertEquals("PAID", response.getStatus());
        assertNotNull(response.getTransactionId());
    }

    // ✅ 2. Successful Card Payment
    @Test
    void shouldProcessCardPaymentSuccessfully() {

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(order.getId());
        request.setPaymentMode("STRIPE");

        PaymentResponseDTO response =
                paymentService.makePayment(user.getId(), request);

        assertEquals("PAID", response.getStatus());
    }

    // ✅ 3. Invalid Payment Mode
    @Test
    void shouldThrowExceptionForInvalidMode() {

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(order.getId());
        request.setPaymentMode("CRYPTO");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> paymentService.makePayment(
                        user.getId(),
                        request
                )
        );

        assertTrue(ex.getMessage().contains("Invalid payment mode"));
    }

    // ✅ 4. Unauthorized User Payment
    @Test
    void shouldBlockUnauthorizedPayment() {

        // another user
        User temp = new User();
        temp.setName("Hacker");
        temp.setEmail("hack@mail.com");
        temp.setPassword("123");

        User other = userRepository.save(temp);

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(order.getId());
        request.setPaymentMode("UPI");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> paymentService.makePayment(
                        other.getId(),
                        request
                )
        );

        assertTrue(
            ex.getMessage().contains("Unauthorized payment attempt")
        );
    }

    // ✅ 5. Order Not Found
    @Test
    void shouldThrowExceptionWhenOrderMissing() {

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(999L);
        request.setPaymentMode("UPI");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> paymentService.makePayment(
                        user.getId(),
                        request
                )
        );

        assertTrue(ex.getMessage().contains("Order not found"));
    }

    // ✅ 6. User Not Found
    @Test
    void shouldThrowExceptionWhenUserMissing() {

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(order.getId());
        request.setPaymentMode("UPI");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> paymentService.makePayment(
                        999L,
                        request
                )
        );

        assertTrue(ex.getMessage().contains("User not found"));
    }

    // ✅ 7. Order Status Updated After Payment
    @Test
    void shouldMarkOrderCompleted() {

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(order.getId());
        request.setPaymentMode("UPI");

        paymentService.makePayment(user.getId(), request);

        Order updated =
                orderRepository.findById(order.getId()).orElseThrow();

        assertEquals("COMPLETED", updated.getStatus());
    }
}