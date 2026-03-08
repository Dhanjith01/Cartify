package com.example.cartify.Security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.cartify.Entity.User;
import com.example.cartify.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"));

        return new CustomUserDetails(user);//org.springframework.security.core.userdetails.User
               // .withUsername(user.getEmail())
               // .password(user.getPassword())
               // .roles(user.getRole())
               // .build();
    }
}
