package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.dto.AuthResponse;
import com.example.musteri_faturalama_sistemi.dto.LoginRequest;
import com.example.musteri_faturalama_sistemi.dto.RegisterRequest;
import com.example.musteri_faturalama_sistemi.entity.User;
import com.example.musteri_faturalama_sistemi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}