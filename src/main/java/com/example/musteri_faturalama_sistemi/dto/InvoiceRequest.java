package com.example.musteri_faturalama_sistemi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    @NotBlank(message = "Fatura numarası boş olamaz")
    private String invoiceNumber;

    @NotNull(message = "Fatura tarihi boş olamaz")
    private LocalDate invoiceDate;

    @NotNull(message = "Tutar boş olamaz")
    @Positive(message = "Tutar pozitif olmalı")
    private BigDecimal amount;

    private boolean paid;
}