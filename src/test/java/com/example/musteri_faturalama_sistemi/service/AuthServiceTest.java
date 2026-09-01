package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.config.JwtUtil;
import com.example.musteri_faturalama_sistemi.dto.AuthResponse;
import com.example.musteri_faturalama_sistemi.dto.LoginRequest;
import com.example.musteri_faturalama_sistemi.dto.RegisterRequest;
import com.example.musteri_faturalama_sistemi.dto.UserResponse;
import com.example.musteri_faturalama_sistemi.entity.User;
import com.example.musteri_faturalama_sistemi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Geçerli bilgilerle kayıt olunduğunda şifre hash'lenmeli ve kullanıcı dönmeli")
    void register_gecerliVeriyle_kullaniciOlusturur() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("melih");
        request.setPassword("sifre123");
        request.setRole("ADMIN");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("melih");
        savedUser.setPassword("HASHLENMIS_SIFRE");
        savedUser.setRole("ADMIN");

        when(passwordEncoder.encode("sifre123")).thenReturn("HASHLENMIS_SIFRE");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = authService.register(request);

        assertEquals("melih", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        // response'ta password alanı hiç yok, bu yüzden onu kontrol etmemize gerek yok
        verify(passwordEncoder, times(1)).encode("sifre123");
    }

    @Test
    @DisplayName("Doğru bilgilerle giriş yapıldığında token dönmeli")
    void login_dogruBilgilerle_tokenDondurur() {
        LoginRequest request = new LoginRequest();
        request.setUsername("melih");
        request.setPassword("sifre123");

        when(jwtUtil.generateToken("melih")).thenReturn("sahte-jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("sahte-jwt-token", response.getToken());
        // authenticate() gerçekten çağrıldı mı, doğrula
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("Yanlış şifreyle giriş yapılmaya çalışılırsa exception fırlatmalı")
    void login_yanlisSifreyle_exceptionFirlatir() {
        LoginRequest request = new LoginRequest();
        request.setUsername("melih");
        request.setPassword("yanlisSifre");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Kullanıcı adı veya şifre hatalı"));

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });

        // Doğrulama başarısız olduğu için, token ÜRETİLMEMELİ
        verify(jwtUtil, never()).generateToken(anyString());
    }
}