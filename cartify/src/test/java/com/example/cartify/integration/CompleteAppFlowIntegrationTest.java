package com.example.cartify.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CompleteAppFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper=new ObjectMapper();

    // ================================
    // HELPERS
    // ================================

    private String registerUser(String email, String password) throws Exception {

        String body = """
        {
          "name": "Test User",
          "email": "%s",
          "password": "%s"
        }
        """.formatted(email, password);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        return body;
    }

    private String loginAndGetToken(String email, String password) throws Exception {

        String loginBody = """
        {
          "email": "%s",
          "password": "%s"
        }
        """.formatted(email, password);

        String response =
                mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        return objectMapper.readTree(response)
                .get("token")
                .asText();
    }

    // ================================
    // FULL FLOW TEST
    // ================================

    @Test
    void completeApplicationFlowTest() throws Exception {

        // =========================================
        // 1️⃣ REGISTER USERS
        // =========================================

        //registerUser("user@test.com", "password");

        // Admin (assume role seeded or configured)
        //registerUser("admin@test.com", "adminpass");

        // =========================================
        // 2️⃣ LOGIN → GET TOKENS
        // =========================================

        String userToken =
                loginAndGetToken("user@test.com", "password");

        String adminToken =
                loginAndGetToken("admin@test.com", "adminpass");

        // =========================================
        // 3️⃣ ADMIN CREATES PRODUCT
        // =========================================

        String productBody = """
        {
          "name": "iPhone 15",
          "description": "Apple Mobile",
          "price": 999.0,
          "stock": 10,
          "category": "Electronics"
        }
        """;

        String productResponse =
                mockMvc.perform(post("/api/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productBody))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        Long productId =
                objectMapper.readTree(productResponse)
                        .get("id")
                        .asLong();

        // =========================================
        // 4️⃣ USER VIEWS PRODUCTS
        // =========================================

        mockMvc.perform(get("/api/products")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        // =========================================
        // 5️⃣ USER ADDS TO CART
        // =========================================

        String cartBody = """
        {
          "productId": %d,
          "quantity": 2
        }
        """.formatted(productId);

        mockMvc.perform(post("/api/cart/add")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartBody))
                .andExpect(status().isOk());

        // =========================================
        // 6️⃣ VIEW CART
        // =========================================

        mockMvc.perform(get("/api/cart")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").exists());

        // =========================================
        // 7️⃣ CHECKOUT → CREATE ORDER
        // =========================================

        String orderResponse =
                mockMvc.perform(post("/api/orders/checkout")
                        .header("Authorization", "Bearer " + userToken))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        Long orderId =
                objectMapper.readTree(orderResponse)
                        .get("id")
                        .asLong();

        // =========================================
        // 8️⃣ MAKE PAYMENT
        // =========================================

        String paymentBody = """
        {
          "orderId": %d,
          "amount": 1998.0,
          "paymentMode": "UPI"
        }
        """.formatted(orderId);

        mockMvc.perform(post("/api/payments/pay")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        // =========================================
        // 9️⃣ USER VIEWS ORDERS
        // =========================================

        mockMvc.perform(get("/api/orders/my")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // =========================================
        // 🔟 ADMIN VIEWS ALL ORDERS
        // =========================================

        mockMvc.perform(get("/api/orders")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
}