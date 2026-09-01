package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.BillingAccountRequest;
import com.example.musteri_faturalama_sistemi.dto.BillingAccountResponse;
import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.Customer;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.repository.BillingAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingAccountServiceTest {

    @Mock
    private BillingAccountRepository billingAccountRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private BillingAccountService billingAccountService;

    @Test
    @DisplayName("Geçerli müşteri ID'siyle hesap oluşturulmalı")
    void createBillingAccount_gecerliMusteri_hesapOlusturur() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Ahmet");

        BillingAccountRequest request = new BillingAccountRequest();
        request.setAccountNumber("ACC-1001");

        BillingAccount savedAccount = new BillingAccount();
        savedAccount.setId(1L);
        savedAccount.setAccountNumber("ACC-1001");
        savedAccount.setCustomer(customer);

        when(customerService.getCustomerEntityById(1L)).thenReturn(customer);
        when(billingAccountRepository.save(any(BillingAccount.class))).thenReturn(savedAccount);

        BillingAccountResponse response = billingAccountService.createBillingAccount(1L, request);

        assertEquals("ACC-1001", response.getAccountNumber());
        assertEquals(1L, response.getCustomerId());
        verify(billingAccountRepository, times(1)).save(any(BillingAccount.class));
    }

    @Test
    @DisplayName("Olmayan müşteri ID'siyle hesap oluşturulmaya çalışılırsa exception fırlatmalı")
    void createBillingAccount_olmayanMusteri_exceptionFirlatir() {
        BillingAccountRequest request = new BillingAccountRequest();
        request.setAccountNumber("ACC-1001");

        when(customerService.getCustomerEntityById(999L))
                .thenThrow(new ResourceNotFoundException("Müşteri bulunamadı: 999"));

        assertThrows(ResourceNotFoundException.class, () -> {
            billingAccountService.createBillingAccount(999L, request);
        });

        // Müşteri bulunamadığı için, hesap kaydetme adımına HİÇ ulaşılmamalı
        verify(billingAccountRepository, never()).save(any(BillingAccount.class));
    }

    @Test
    @DisplayName("Hesap ID ile bulunduğunda doğru bilgiyi döndürmeli")
    void getBillingAccountById_hesapVarsa_dogruBilgiyiDondurur() {
        Customer customer = new Customer();
        customer.setId(1L);

        BillingAccount account = new BillingAccount();
        account.setId(1L);
        account.setAccountNumber("ACC-1001");
        account.setCustomer(customer);

        when(billingAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        BillingAccountResponse response = billingAccountService.getBillingAccountById(1L);

        assertEquals("ACC-1001", response.getAccountNumber());
    }

    @Test
    @DisplayName("Olmayan hesap ID'si sorgulandığında exception fırlatmalı")
    void getBillingAccountById_hesapYoksa_exceptionFirlatir() {
        when(billingAccountRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            billingAccountService.getBillingAccountById(999L);
        });
    }

    @Test
    @DisplayName("Hesap silindiğinde repository'nin deleteById metodu çağrılmalı")
    void deleteBillingAccount_gecerliId_deleteByIdCagirir() {
        billingAccountService.deleteBillingAccount(1L);

        verify(billingAccountRepository, times(1)).deleteById(1L);
    }
}