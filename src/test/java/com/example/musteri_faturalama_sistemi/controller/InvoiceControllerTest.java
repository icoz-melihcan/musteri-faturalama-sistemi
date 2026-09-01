package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.config.JwtAuthFilter;
import com.example.musteri_faturalama_sistemi.config.SecurityConfig;
import com.example.musteri_faturalama_sistemi.dto.InvoiceRequest;
import com.example.musteri_faturalama_sistemi.dto.InvoiceResponse;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.service.InvoiceService;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = InvoiceController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InvoiceService invoiceService;

    @Test
    @DisplayName("POST /api/billing-accounts/{billingAccountId}/invoices - geçerli veriyle 200 dönmeli")
    void createInvoice_gecerliVeriyle_200Doner() throws Exception {
        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceNumber("INV-2026-001");
        request.setInvoiceDate(LocalDate.of(2026, 8, 28));
        request.setAmount(new BigDecimal("149.90"));
        request.setPaid(false);

        InvoiceResponse response = new InvoiceResponse(
                1L, "INV-2026-001", LocalDate.of(2026, 8, 28), new BigDecimal("149.90"), false, 1L);
        when(invoiceService.createInvoice(anyLong(), any(InvoiceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/billing-accounts/1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-2026-001"));
    }

    @Test
    @DisplayName("POST /api/billing-accounts/{billingAccountId}/invoices - negatif tutarla 400 dönmeli")
    void createInvoice_negatifTutar_400Doner() throws Exception {
        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceNumber("INV-2026-001");
        request.setInvoiceDate(LocalDate.of(2026, 8, 28));
        request.setAmount(new BigDecimal("-50"));

        mockMvc.perform(post("/api/billing-accounts/1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/invoices/{id} - fatura yoksa 404 dönmeli")
    void getInvoiceById_faturaYoksa_404Doner() throws Exception {
        when(invoiceService.getInvoiceById(999L))
                .thenThrow(new ResourceNotFoundException("Fatura bulunamadı: 999"));

        mockMvc.perform(get("/api/invoices/999"))
                .andExpect(status().isNotFound());
    }
}