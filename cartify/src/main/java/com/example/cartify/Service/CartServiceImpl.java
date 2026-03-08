package com.example.cartify.Service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cartify.DTO.CartItemDTO;
import com.example.cartify.DTO.CartItemResponseDTO;
import com.example.cartify.DTO.CartResponseDTO;
import com.example.cartify.Entity.Cart;
import com.example.cartify.Entity.CartItem;
import com.example.cartify.Entity.Product;
import com.example.cartify.Entity.User;
import com.example.cartify.Repository.CartItemRepository;
import com.example.cartify.Repository.CartRepository;
import com.example.cartify.Repository.ProductRepository;
import com.example.cartify.Repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final CartItemRepository itemRepo;
    private final UserRepository userRepo;

    public CartServiceImpl(CartRepository cartRepo,
                           ProductRepository productRepo,
                           CartItemRepository itemRepo,
                           UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.itemRepo = itemRepo;
        this.userRepo = userRepo;
    }

    private Cart getOrCreateCart(User user) {
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepo.save(c);
        });
    }

    @Override
    public CartResponseDTO addItem(Long userId, CartItemDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = getOrCreateCart(user);

        // Check if item exists
        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + dto.getQuantity());
            existing.setPrice(existing.getQuantity() * product.getPrice());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(product.getPrice() * dto.getQuantity());
            cart.getItems().add(item);
        }

        cartRepo.save(cart);
        return mapCart(cart);
    }

    @Override
    public CartResponseDTO removeItem(Long userId, Long productId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = getOrCreateCart(user);
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        cartRepo.save(cart);
        return mapCart(cart);
    }

    @Override
    public CartResponseDTO updateQuantity(Long userId, Long productId, int quantity) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = getOrCreateCart(user);
        cart.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(i -> {
                i.setQuantity(quantity);
                i.setPrice(quantity * i.getProduct().getPrice());
            });
        cartRepo.save(cart);
        return mapCart(cart);
    }

    @Override
    public CartResponseDTO getCart(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = getOrCreateCart(user);
        return mapCart(cart);
    }

    private CartResponseDTO mapCart(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setItems(cart.getItems().stream().map(i -> {
            CartItemResponseDTO itemDto = new CartItemResponseDTO();
            itemDto.setProductId(i.getProduct().getId());
            itemDto.setProductName(i.getProduct().getName());
            itemDto.setQuantity(i.getQuantity());
            itemDto.setPrice(i.getPrice());
            return itemDto;
        }).collect(Collectors.toList()));
        dto.setTotalPrice(cart.getItems().stream().mapToDouble(CartItem::getPrice).sum());
        return dto;
    }
}
