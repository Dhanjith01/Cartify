package com.example.cartify.Controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cartify.DTO.AuthResponseDTO;
import com.example.cartify.DTO.LoginRequestDTO;
import com.example.cartify.Entity.User;
import com.example.cartify.Repository.UserRepository;
import com.example.cartify.Security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public AuthController(UserRepository repo,
                          JwtUtil jwtUtil) {
        this.userRepository = repo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @RequestBody LoginRequestDTO request) {

        User user =
            userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                new RuntimeException("Invalid email"));

        if (!encoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token =
                jwtUtil.generateToken(
                        user.getEmail(),
                        user.getRole());

        return new AuthResponseDTO(
                token,
                user.getEmail(),
                user.getRole());
    }
}