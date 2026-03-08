package com.example.cartify.DTO;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public UserResponseDTO(Long id,
                           String name,
                           String email,
                           String phone,
                           String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
}
