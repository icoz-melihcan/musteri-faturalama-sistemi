package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.BillingAddressRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAddressResponse;
import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.BillingAddress;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.repository.BillingAddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingAddressServiceTest {

    @Mock
    private BillingAddressRepository billingAddressRepository;

    @Mock
    private BillingAccountService billingAccountService;

    @InjectMocks
    private BillingAddressService billingAddressService;

    @Test
    @DisplayName("Geçerli hesap ID'siyle adres oluşturulmalı")
    void createBillingAddress_gecerliHesap_adresOlusturur() {
        BillingAccount account = new BillingAccount();
        account.setId(1L);

        BillingAddressRequest request = new BillingAddressRequest();
        request.setCity("Ankara");
        request.setDistrict("Çankaya");
        request.setFullAddress("Teknokent Cad. No:5");
        request.setPostalCode("06800");

        BillingAddress savedAddress = new BillingAddress();
        savedAddress.setId(1L);
        savedAddress.setCity("Ankara");
        savedAddress.setDistrict("Çankaya");
        savedAddress.setFullAddress("Teknokent Cad. No:5");
        savedAddress.setPostalCode("06800");
        savedAddress.setBillingAccount(account);

        when(billingAccountService.getBillingAccountEntityById(1L)).thenReturn(account);
        when(billingAddressRepository.save(any(BillingAddress.class))).thenReturn(savedAddress);

        BillingAddressResponse response = billingAddressService.createBillingAddress(1L, request);

        assertEquals("Ankara", response.getCity());
        assertEquals(1L, response.getBillingAccountId());
        verify(billingAddressRepository, times(1)).save(any(BillingAddress.class));
    }

    @Test
    @DisplayName("Olmayan hesap ID'siyle adres oluşturulmaya çalışılırsa exception fırlatmalı")
    void createBillingAddress_olmayanHesap_exceptionFirlatir() {
        BillingAddressRequest request = new BillingAddressRequest();
        request.setCity("Ankara");

        when(billingAccountService.getBillingAccountEntityById(999L))
                .thenThrow(new ResourceNotFoundException("Hesap bulunamadı: 999"));

        assertThrows(ResourceNotFoundException.class, () -> {
            billingAddressService.createBillingAddress(999L, request);
        });

        verify(billingAddressRepository, never()).save(any(BillingAddress.class));
    }

    @Test
    @DisplayName("Adres ID ile bulunduğunda doğru bilgiyi döndürmeli")
    void getBillingAddressById_adresVarsa_dogruBilgiyiDondurur() {
        BillingAccount account = new BillingAccount();
        account.setId(1L);

        BillingAddress address = new BillingAddress();
        address.setId(1L);
        address.setCity("İstanbul");
        address.setBillingAccount(account);

        when(billingAddressRepository.findById(1L)).thenReturn(Optional.of(address));

        BillingAddressResponse response = billingAddressService.getBillingAddressById(1L);

        assertEquals("İstanbul", response.getCity());
    }

    @Test
    @DisplayName("Olmayan adres ID'si sorgulandığında exception fırlatmalı")
    void getBillingAddressById_adresYoksa_exceptionFirlatir() {
        when(billingAddressRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            billingAddressService.getBillingAddressById(999L);
        });
    }

    @Test
    @DisplayName("Adres silindiğinde repository'nin deleteById metodu çağrılmalı")
    void deleteBillingAddress_gecerliId_deleteByIdCagirir() {
        billingAddressService.deleteBillingAddress(1L);

        verify(billingAddressRepository, times(1)).deleteById(1L);
    }
}