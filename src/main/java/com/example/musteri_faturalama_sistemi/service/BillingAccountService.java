package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.BillingAccountRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAccountResponse;
import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.Customer;
import com.example.musteri_faturalama_sistemi.repository.BillingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingAccountService {

    @Autowired
    private BillingAccountRepository billingAccountRepository;

    @Autowired
    private CustomerService customerService;

    public List<BillingAccountResponse> getAllBillingAccounts() {
        return billingAccountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BillingAccountResponse getBillingAccountById(Long id) {
        return toResponse(getBillingAccountEntityById(id));
    }

    public BillingAccountResponse createBillingAccount(Long customerId, BillingAccountRequest request) {
        Customer customer = customerService.getCustomerEntityById(customerId);

        BillingAccount billingAccount = new BillingAccount();
        billingAccount.setAccountNumber(request.getAccountNumber());
        billingAccount.setCustomer(customer);

        BillingAccount saved = billingAccountRepository.save(billingAccount);
        return toResponse(saved);
    }

    public BillingAccountResponse updateBillingAccount(Long id, BillingAccountRequest request) {
        BillingAccount existing = getBillingAccountEntityById(id);
        existing.setAccountNumber(request.getAccountNumber());
        BillingAccount updated = billingAccountRepository.save(existing);
        return toResponse(updated);
    }

    public void deleteBillingAccount(Long id) {
        billingAccountRepository.deleteById(id);
    }

    // --- Yardımcı metotlar ---

    public BillingAccount getBillingAccountEntityById(Long id) {
        return billingAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı: " + id));
    }

    private BillingAccountResponse toResponse(BillingAccount billingAccount) {
        return new BillingAccountResponse(
                billingAccount.getId(),
                billingAccount.getAccountNumber(),
                billingAccount.getCustomer().getId()
        );
    }
}