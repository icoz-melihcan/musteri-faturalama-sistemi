// dto/InvoiceRequest.java
package com.example.musteri_faturalama_sistemi.dto;

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
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal amount;
    private boolean paid;
}