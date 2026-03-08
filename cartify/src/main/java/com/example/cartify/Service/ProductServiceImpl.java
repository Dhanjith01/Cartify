package com.example.cartify.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cartify.DTO.ProductRequestDTO;
import com.example.cartify.DTO.ProductResponseDTO;
import com.example.cartify.Entity.Product;
import com.example.cartify.Repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO req) {
        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setCategory(req.getCategory());
        product.setImageUrl(req.getImageUrl());

        Product saved = repo.save(product);
        return mapToDTO(saved);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO req) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setCategory(req.getCategory());
        product.setImageUrl(req.getImageUrl());

        Product saved = repo.save(product);
        return mapToDTO(saved);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    @Override
    public ProductResponseDTO getProduct(Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return repo.findAll().stream().map(product -> mapToDTO(product)).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return repo.findByCategory(category).stream().map(product -> mapToDTO(product)).collect(Collectors.toList());
    }

    private ProductResponseDTO mapToDTO(Product p) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setCategory(p.getCategory());
        dto.setImageUrl(p.getImageUrl());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}