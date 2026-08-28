package com.example.musteri_faturalama_sistemi.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddressRequest {

    @NotBlank(message = "Şehir boş olamaz")
    private String city;

    @NotBlank(message = "İlçe boş olamaz")
    private String district;

    @NotBlank(message = "Adres boş olamaz")
    private String fullAddress;

    @Pattern(regexp = "^[0-9]{5}$", message = "Posta kodu 5 haneli olmalı")
    private String postalCode;
}