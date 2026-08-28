package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.dto.AuthResponse;
import com.example.musteri_faturalama_sistemi.dto.LoginRequest;
import com.example.musteri_faturalama_sistemi.dto.RegisterRequest;
import com.example.musteri_faturalama_sistemi.dto.UserResponse;
import com.example.musteri_faturalama_sistemi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}