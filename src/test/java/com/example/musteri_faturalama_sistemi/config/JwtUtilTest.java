package com.example.musteri_faturalama_sistemi.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // @Value ile enjekte edilen alanlara, test ortamında elle değer veriyoruz
        ReflectionTestUtils.setField(jwtUtil, "secret", "bu-cok-gizli-bir-anahtar-en-az-32-karakter-olmali-guvenlik-icin-uzun-tutuyoruz");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    @DisplayName("Token üretildiğinde boş olmamalı")
    void generateToken_gecerliUsername_tokenUretir() {
        String token = jwtUtil.generateToken("melih");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Üretilen token'dan doğru username çıkarılmalı")
    void extractUsername_gecerliToken_dogruUsernameDondurur() {
        String token = jwtUtil.generateToken("melih");

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals("melih", extractedUsername);
    }

    @Test
    @DisplayName("Doğru username ile token geçerli sayılmalı")
    void isTokenValid_dogruUsername_trueDondurur() {
        String token = jwtUtil.generateToken("melih");

        boolean isValid = jwtUtil.isTokenValid(token, "melih");

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Yanlış username ile token geçersiz sayılmalı")
    void isTokenValid_yanlisUsername_falseDondurur() {
        String token = jwtUtil.generateToken("melih");

        boolean isValid = jwtUtil.isTokenValid(token, "baskaKullanici");

        assertFalse(isValid);
    }
}