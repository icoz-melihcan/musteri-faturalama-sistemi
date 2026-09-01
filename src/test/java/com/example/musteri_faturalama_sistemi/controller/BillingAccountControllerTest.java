package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.config.JwtAuthFilter;
import com.example.musteri_faturalama_sistemi.config.SecurityConfig;
import com.example.musteri_faturalama_sistemi.dto.BillingAccountRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAccountResponse;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.service.BillingAccountService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BillingAccountController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
class BillingAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BillingAccountService billingAccountService;

    @Test
    @DisplayName("POST /api/customers/{customerId}/billing-accounts - geçerli veriyle 200 dönmeli")
    void createBillingAccount_gecerliVeriyle_200Doner() throws Exception {
        BillingAccountRequest request = new BillingAccountRequest();
        request.setAccountNumber("ACC-1001");

        BillingAccountResponse response = new BillingAccountResponse(1L, "ACC-1001", 1L);
        when(billingAccountService.createBillingAccount(anyLong(), any(BillingAccountRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/customers/1/billing-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC-1001"));
    }

    @Test
    @DisplayName("POST /api/customers/{customerId}/billing-accounts - boş accountNumber ile 400 dönmeli")
    void createBillingAccount_bosAccountNumber_400Doner() throws Exception {
        BillingAccountRequest request = new BillingAccountRequest();
        request.setAccountNumber("");

        mockMvc.perform(post("/api/customers/1/billing-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/billing-accounts/{id} - hesap yoksa 404 dönmeli")
    void getBillingAccountById_hesapYoksa_404Doner() throws Exception {
        when(billingAccountService.getBillingAccountById(999L))
                .thenThrow(new ResourceNotFoundException("Hesap bulunamadı: 999"));

        mockMvc.perform(get("/api/billing-accounts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/billing-accounts/{id} - başarılı silme 200 dönmeli")
    void deleteBillingAccount_gecerliId_200Doner() throws Exception {
        mockMvc.perform(delete("/api/billing-accounts/1"))
                .andExpect(status().isOk());
    }
}