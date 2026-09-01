package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.config.JwtAuthFilter;
import com.example.musteri_faturalama_sistemi.config.SecurityConfig;
import com.example.musteri_faturalama_sistemi.dto.AuthResponse;
import com.example.musteri_faturalama_sistemi.dto.LoginRequest;
import com.example.musteri_faturalama_sistemi.dto.RegisterRequest;
import com.example.musteri_faturalama_sistemi.dto.UserResponse;
import com.example.musteri_faturalama_sistemi.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("POST /api/auth/register - geçerli veriyle 200 dönmeli")
    void register_gecerliVeriyle_200Doner() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("melih");
        request.setPassword("sifre123");
        request.setRole("ADMIN");

        UserResponse response = new UserResponse(1L, "melih", "ADMIN");
        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("melih"));
    }

    @Test
    @DisplayName("POST /api/auth/register - kısa şifreyle 400 dönmeli")
    void register_kisaSifreyle_400Doner() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("melih");
        request.setPassword("123");
        request.setRole("ADMIN");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - geçerli bilgilerle 200 ve token dönmeli")
    void login_gecerliBilgilerle_200DonerVeTokenVerir() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("melih");
        request.setPassword("sifre123");

        AuthResponse response = new AuthResponse("sahte-jwt-token");
        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("sahte-jwt-token"));
    }
}