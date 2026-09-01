package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.config.JwtAuthFilter;
import com.example.musteri_faturalama_sistemi.config.SecurityConfig;
import com.example.musteri_faturalama_sistemi.dto.BillingAddressRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAddressResponse;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.service.BillingAddressService;
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
        controllers = BillingAddressController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
class BillingAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BillingAddressService billingAddressService;

    @Test
    @DisplayName("POST /api/billing-accounts/{billingAccountId}/address - geçerli veriyle 200 dönmeli")
    void createBillingAddress_gecerliVeriyle_200Doner() throws Exception {
        BillingAddressRequest request = new BillingAddressRequest();
        request.setCity("Ankara");
        request.setDistrict("Çankaya");
        request.setFullAddress("Teknokent Cad. No:5");
        request.setPostalCode("06800");

        BillingAddressResponse response =
                new BillingAddressResponse(1L, "Ankara", "Çankaya", "Teknokent Cad. No:5", "06800", 1L);
        when(billingAddressService.createBillingAddress(anyLong(), any(BillingAddressRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/billing-accounts/1/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Ankara"));
    }

    @Test
    @DisplayName("POST /api/billing-accounts/{billingAccountId}/address - geçersiz posta koduyla 400 dönmeli")
    void createBillingAddress_gecersizPostaKodu_400Doner() throws Exception {
        BillingAddressRequest request = new BillingAddressRequest();
        request.setCity("Ankara");
        request.setDistrict("Çankaya");
        request.setFullAddress("Teknokent Cad. No:5");
        request.setPostalCode("123");

        mockMvc.perform(post("/api/billing-accounts/1/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/billing-addresses/{id} - adres yoksa 404 dönmeli")
    void getBillingAddressById_adresYoksa_404Doner() throws Exception {
        when(billingAddressService.getBillingAddressById(999L))
                .thenThrow(new ResourceNotFoundException("Adres bulunamadı: 999"));

        mockMvc.perform(get("/api/billing-addresses/999"))
                .andExpect(status().isNotFound());
    }
}