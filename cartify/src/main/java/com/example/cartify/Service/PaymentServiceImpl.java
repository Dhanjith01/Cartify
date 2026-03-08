package com.example.cartify.Service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.cartify.Repository.OrderRepository;
import com.example.cartify.Repository.UserRepository;
import com.example.cartify.DTO.PaymentRequestDTO;
import com.example.cartify.DTO.PaymentResponseDTO;
import com.example.cartify.Entity.Order;
import com.example.cartify.Entity.Payment;
import com.example.cartify.Entity.User;
import com.example.cartify.PaymentStrategy.PaymentStrategy;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final Map<String, PaymentStrategy> strategyMap;

    public PaymentServiceImpl(OrderRepository orderRepo,
                              UserRepository userRepo,
                              Map<String, PaymentStrategy> strategyMap) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.strategyMap = strategyMap;
    }

    @Override
    public PaymentResponseDTO makePayment(Long userId,
                                          PaymentRequestDTO request) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized payment attempt");
        }

        // 🔹 Strategy selection
        PaymentStrategy strategy =
                strategyMap.get(request.getPaymentMode());

        if (strategy == null) {
            throw new RuntimeException("Invalid payment mode");
        }

        Payment payment = strategy.processPayment(order);

        // Update order
        order.setStatus("COMPLETED");
        orderRepo.save(order);

        return mapToDTO(payment);
    }

    private PaymentResponseDTO mapToDTO(Payment payment) {

        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setPaymentId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt());

        return dto;
    }
}