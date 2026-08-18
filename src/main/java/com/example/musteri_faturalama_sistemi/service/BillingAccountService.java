package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.Customer;
import com.example.musteri_faturalama_sistemi.repository.BillingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingAccountService {

    @Autowired
    private BillingAccountRepository billingAccountRepository;

    @Autowired
    private CustomerService customerService;

    public List<BillingAccount> getAllBillingAccounts() {
        return billingAccountRepository.findAll();
    }

    public BillingAccount getBillingAccountById(Long id) {
        return billingAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı: " + id));
    }

    public BillingAccount createBillingAccount(Long customerId, BillingAccount billingAccount) {
        Customer customer = customerService.getCustomerById(customerId);
        billingAccount.setCustomer(customer);
        return billingAccountRepository.save(billingAccount);
    }

    public BillingAccount updateBillingAccount(Long id, BillingAccount updatedAccount) {
        BillingAccount existing = getBillingAccountById(id);
        existing.setAccountNumber(updatedAccount.getAccountNumber());
        return billingAccountRepository.save(existing);
    }

    public void deleteBillingAccount(Long id) {
        billingAccountRepository.deleteById(id);
    }
}