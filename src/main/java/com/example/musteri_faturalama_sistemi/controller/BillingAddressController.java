package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.entity.BillingAddress;
import com.example.musteri_faturalama_sistemi.service.BillingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillingAddressController {

    @Autowired
    private BillingAddressService billingAddressService;

    @PostMapping("/api/billing-accounts/{billingAccountId}/address")
    public BillingAddress createBillingAddress(@PathVariable Long billingAccountId,
                                               @RequestBody BillingAddress billingAddress) {
        return billingAddressService.createBillingAddress(billingAccountId, billingAddress);
    }

    @GetMapping("/api/billing-addresses")
    public List<BillingAddress> getAllBillingAddresses() {
        return billingAddressService.getAllBillingAddresses();
    }

    @GetMapping("/api/billing-addresses/{id}")
    public BillingAddress getBillingAddressById(@PathVariable Long id) {
        return billingAddressService.getBillingAddressById(id);
    }

    @PutMapping("/api/billing-addresses/{id}")
    public BillingAddress updateBillingAddress(@PathVariable Long id,
                                               @RequestBody BillingAddress billingAddress) {
        return billingAddressService.updateBillingAddress(id, billingAddress);
    }

    @DeleteMapping("/api/billing-addresses/{id}")
    public void deleteBillingAddress(@PathVariable Long id) {
        billingAddressService.deleteBillingAddress(id);
    }
}