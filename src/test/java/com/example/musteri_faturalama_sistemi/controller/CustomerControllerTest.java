package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.config.JwtAuthFilter;
import com.example.musteri_faturalama_sistemi.config.SecurityConfig;
import com.example.musteri_faturalama_sistemi.dto.CustomerRequest;
import com.example.musteri_faturalama_sistemi.dto.CustomerResponse;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.service.CustomerService;
import tools.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CustomerController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    @DisplayName("GET /api/customers/{id} - müşteri varsa 200 dönmeli")
    void getCustomerById_musteriVarsa_200Doner() throws Exception {
        CustomerResponse response = new CustomerResponse(1L, "Ahmet", "Yılmaz", "ahmet@example.com", "05551234567");
        when(customerService.getCustomerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ahmet"))
                .andExpect(jsonPath("$.email").value("ahmet@example.com"));
    }

    @Test
    @DisplayName("GET /api/customers/{id} - müşteri yoksa 404 dönmeli")
    void getCustomerById_musteriYoksa_404Doner() throws Exception {
        when(customerService.getCustomerById(999L))
                .thenThrow(new ResourceNotFoundException("Müşteri bulunamadı: 999"));

        mockMvc.perform(get("/api/customers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Müşteri bulunamadı: 999"));
    }

    @Test
    @DisplayName("GET /api/customers - tüm müşteriler listelenmeli")
    void getAllCustomers_200DonerVeListeVerir() throws Exception {
        CustomerResponse response = new CustomerResponse(1L, "Ahmet", "Yılmaz", "ahmet@example.com", "05551234567");
        when(customerService.getAllCustomers()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Ahmet"));
    }

    @Test
    @DisplayName("POST /api/customers - geçerli veriyle 200 dönmeli")
    void createCustomer_gecerliVeriyle_200Doner() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName("Mehmet");
        request.setLastName("Demir");
        request.setEmail("mehmet@example.com");
        request.setPhoneNumber("05559876543");

        CustomerResponse response = new CustomerResponse(2L, "Mehmet", "Demir", "mehmet@example.com", "05559876543");
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mehmet"));
    }

    @Test
    @DisplayName("POST /api/customers - boş isimle 400 dönmeli (validation)")
    void createCustomer_boşIsimle_400Doner() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName("");
        request.setLastName("Demir");
        request.setEmail("gecersiz-email");
        request.setPhoneNumber("abc");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}