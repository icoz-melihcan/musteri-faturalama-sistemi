package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.BillingAddress;
import com.example.musteri_faturalama_sistemi.repository.BillingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingAddressService {

    @Autowired
    private BillingAddressRepository billingAddressRepository;

    @Autowired
    private BillingAccountService billingAccountService;

    public List<BillingAddress> getAllBillingAddresses() {
        return billingAddressRepository.findAll();
    }

    public BillingAddress getBillingAddressById(Long id) {
        return billingAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adres bulunamadı: " + id));
    }

    public BillingAddress createBillingAddress(Long billingAccountId, BillingAddress billingAddress) {
        BillingAccount billingAccount = billingAccountService.getBillingAccountById(billingAccountId);
        billingAddress.setBillingAccount(billingAccount);
        return billingAddressRepository.save(billingAddress);
    }

    public BillingAddress updateBillingAddress(Long id, BillingAddress updatedAddress) {
        BillingAddress existing = getBillingAddressById(id);
        existing.setCity(updatedAddress.getCity());
        existing.setDistrict(updatedAddress.getDistrict());
        existing.setFullAddress(updatedAddress.getFullAddress());
        existing.setPostalCode(updatedAddress.getPostalCode());
        return billingAddressRepository.save(existing);
    }

    public void deleteBillingAddress(Long id) {
        billingAddressRepository.deleteById(id);
    }
}