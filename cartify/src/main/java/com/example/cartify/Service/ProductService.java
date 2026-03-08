package com.example.cartify.Service;



import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cartify.DTO.ProductRequestDTO;
import com.example.cartify.DTO.ProductResponseDTO;

@Service
public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO request);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO request);
    void deleteProduct(Long id);
    ProductResponseDTO getProduct(Long id);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getProductsByCategory(String category);
}

