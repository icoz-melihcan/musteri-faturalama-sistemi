package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.dto.BillingAddressRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAddressResponse;
import com.example.musteri_faturalama_sistemi.service.BillingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillingAddressController {

    @Autowired
    private BillingAddressService billingAddressService;

    @PostMapping("/api/billing-accounts/{billingAccountId}/address")
    public BillingAddressResponse createBillingAddress(@PathVariable Long billingAccountId,
                                                       @RequestBody BillingAddressRequest request) {
        return billingAddressService.createBillingAddress(billingAccountId, request);
    }

    @GetMapping("/api/billing-addresses")
    public List<BillingAddressResponse> getAllBillingAddresses() {
        return billingAddressService.getAllBillingAddresses();
    }

    @GetMapping("/api/billing-addresses/{id}")
    public BillingAddressResponse getBillingAddressById(@PathVariable Long id) {
        return billingAddressService.getBillingAddressById(id);
    }

    @PutMapping("/api/billing-addresses/{id}")
    public BillingAddressResponse updateBillingAddress(@PathVariable Long id,
                                                       @RequestBody BillingAddressRequest request) {
        return billingAddressService.updateBillingAddress(id, request);
    }

    @DeleteMapping("/api/billing-addresses/{id}")
    public void deleteBillingAddress(@PathVariable Long id) {
        billingAddressService.deleteBillingAddress(id);
    }
}