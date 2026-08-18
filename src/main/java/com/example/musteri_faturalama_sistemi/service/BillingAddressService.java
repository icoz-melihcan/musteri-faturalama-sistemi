package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.BillingAddressRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAddressResponse;
import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.BillingAddress;
import com.example.musteri_faturalama_sistemi.repository.BillingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingAddressService {

    @Autowired
    private BillingAddressRepository billingAddressRepository;

    @Autowired
    private BillingAccountService billingAccountService;

    public List<BillingAddressResponse> getAllBillingAddresses() {
        return billingAddressRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BillingAddressResponse getBillingAddressById(Long id) {
        return toResponse(getBillingAddressEntityById(id));
    }

    public BillingAddressResponse createBillingAddress(Long billingAccountId, BillingAddressRequest request) {
        BillingAccount billingAccount = billingAccountService.getBillingAccountEntityById(billingAccountId);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setCity(request.getCity());
        billingAddress.setDistrict(request.getDistrict());
        billingAddress.setFullAddress(request.getFullAddress());
        billingAddress.setPostalCode(request.getPostalCode());
        billingAddress.setBillingAccount(billingAccount);

        BillingAddress saved = billingAddressRepository.save(billingAddress);
        return toResponse(saved);
    }

    public BillingAddressResponse updateBillingAddress(Long id, BillingAddressRequest request) {
        BillingAddress existing = getBillingAddressEntityById(id);
        existing.setCity(request.getCity());
        existing.setDistrict(request.getDistrict());
        existing.setFullAddress(request.getFullAddress());
        existing.setPostalCode(request.getPostalCode());
        BillingAddress updated = billingAddressRepository.save(existing);
        return toResponse(updated);
    }

    public void deleteBillingAddress(Long id) {
        billingAddressRepository.deleteById(id);
    }

    // --- Yardımcı metotlar ---

    public BillingAddress getBillingAddressEntityById(Long id) {
        return billingAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adres bulunamadı: " + id));
    }

    private BillingAddressResponse toResponse(BillingAddress billingAddress) {
        return new BillingAddressResponse(
                billingAddress.getId(),
                billingAddress.getCity(),
                billingAddress.getDistrict(),
                billingAddress.getFullAddress(),
                billingAddress.getPostalCode(),
                billingAddress.getBillingAccount().getId()
        );
    }
}