package com.example.cartify.Service;

import org.springframework.stereotype.Service;

import com.example.cartify.DTO.UserRequestDTO;
import com.example.cartify.DTO.UserResponseDTO;

@Service
public interface UserService {

    UserResponseDTO registerUser(UserRequestDTO request);

    UserResponseDTO getUserById(Long id);
}