package com.example.cartify.Security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.cartify.Entity.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final Long id;

    public CustomUserDetails(User user) {
        super(user.getEmail(), user.getPassword(),
              true, true, true, true, // flags
              List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        this.id = user.getId();
    }

    public Long getId() { return id; }
}