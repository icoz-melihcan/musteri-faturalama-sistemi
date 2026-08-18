package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.dto.BillingAccountRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAccountResponse;
import com.example.musteri_faturalama_sistemi.service.BillingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillingAccountController {

    @Autowired
    private BillingAccountService billingAccountService;

    @PostMapping("/api/customers/{customerId}/billing-accounts")
    public BillingAccountResponse createBillingAccount(@PathVariable Long customerId,
                                                       @RequestBody BillingAccountRequest request) {
        return billingAccountService.createBillingAccount(customerId, request);
    }

    @GetMapping("/api/billing-accounts")
    public List<BillingAccountResponse> getAllBillingAccounts() {
        return billingAccountService.getAllBillingAccounts();
    }

    @GetMapping("/api/billing-accounts/{id}")
    public BillingAccountResponse getBillingAccountById(@PathVariable Long id) {
        return billingAccountService.getBillingAccountById(id);
    }

    @PutMapping("/api/billing-accounts/{id}")
    public BillingAccountResponse updateBillingAccount(@PathVariable Long id,
                                                       @RequestBody BillingAccountRequest request) {
        return billingAccountService.updateBillingAccount(id, request);
    }

    @DeleteMapping("/api/billing-accounts/{id}")
    public void deleteBillingAccount(@PathVariable Long id) {
        billingAccountService.deleteBillingAccount(id);
    }
}