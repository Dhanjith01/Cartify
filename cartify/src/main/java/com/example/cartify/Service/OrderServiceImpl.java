package com.example.cartify.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cartify.DTO.CartItemResponseDTO;
import com.example.cartify.DTO.CartResponseDTO;
import com.example.cartify.DTO.OrderResponseDTO;
import com.example.cartify.Entity.Order;
import com.example.cartify.Entity.OrderItem;
import com.example.cartify.Entity.Product;
import com.example.cartify.Entity.User;
import com.example.cartify.Repository.CartRepository;
import com.example.cartify.Repository.OrderRepository;
import com.example.cartify.Repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final CartService cartService;
    private final UserRepository userRepo;

    public OrderServiceImpl(OrderRepository orderRepo,
                            CartRepository cartRepo,
                            CartService cartService,
                            UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.cartService = cartService;
        this.userRepo = userRepo;
    }

    @Override
    public OrderResponseDTO checkout(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get user cart
        CartResponseDTO cart = cartService.getCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getTotalPrice());

        // Convert cart items to order items
        for (CartItemResponseDTO itemDto : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            Product product = new Product();
            product.setId(itemDto.getProductId());
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(itemDto.getPrice());
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        }

        orderRepo.save(order);

        // Clear user cart
        cartRepo.findByUser(user).ifPresent(c -> {
            c.getItems().clear();
            cartRepo.save(c);
        });

        return mapOrder(order);
    }

    @Override
    public List<OrderResponseDTO> getUserOrders(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepo.findByUser(user).stream().map(this::mapOrder).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepo.findAll().stream().map(this::mapOrder).collect(Collectors.toList());
    }

    private OrderResponseDTO mapOrder(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream().map(i -> {
            CartItemResponseDTO itemDto = new CartItemResponseDTO();
            itemDto.setProductId(i.getProduct().getId());
            itemDto.setProductName(i.getProduct().getName());
            itemDto.setQuantity(i.getQuantity());
            itemDto.setPrice(i.getPrice());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
