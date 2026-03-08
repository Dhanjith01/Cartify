package com.example.cartify.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cartify.DTO.ProductRequestDTO;
import com.example.cartify.DTO.ProductResponseDTO;
import com.example.cartify.Service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // Admin only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDTO createProduct(@RequestBody ProductRequestDTO request) {
        return service.createProduct(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDTO updateProduct(@PathVariable Long id,
                                            @RequestBody ProductRequestDTO request) {
        return service.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }

    // Public / Users
    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/category/{category}")
    public List<ProductResponseDTO> getByCategory(@PathVariable String category) {
        return service.getProductsByCategory(category);
    }
}