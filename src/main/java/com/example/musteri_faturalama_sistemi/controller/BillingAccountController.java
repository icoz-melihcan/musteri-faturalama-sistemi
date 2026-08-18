package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.service.BillingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillingAccountController {

    @Autowired
    private BillingAccountService billingAccountService;

    @PostMapping("/api/customers/{customerId}/billing-accounts")
    public BillingAccount createBillingAccount(@PathVariable Long customerId,
                                               @RequestBody BillingAccount billingAccount) {
        return billingAccountService.createBillingAccount(customerId, billingAccount);
    }

    @GetMapping("/api/billing-accounts")
    public List<BillingAccount> getAllBillingAccounts() {
        return billingAccountService.getAllBillingAccounts();
    }

    @GetMapping("/api/billing-accounts/{id}")
    public BillingAccount getBillingAccountById(@PathVariable Long id) {
        return billingAccountService.getBillingAccountById(id);
    }

    @PutMapping("/api/billing-accounts/{id}")
    public BillingAccount updateBillingAccount(@PathVariable Long id,
                                               @RequestBody BillingAccount billingAccount) {
        return billingAccountService.updateBillingAccount(id, billingAccount);
    }

    @DeleteMapping("/api/billing-accounts/{id}")
    public void deleteBillingAccount(@PathVariable Long id) {
        billingAccountService.deleteBillingAccount(id);
    }
}