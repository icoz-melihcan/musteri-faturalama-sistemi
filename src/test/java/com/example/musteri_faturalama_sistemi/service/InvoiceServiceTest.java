package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.InvoiceRequest;
import com.example.musteri_faturalama_sistemi.dto.InvoiceResponse;
import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.Invoice;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.repository.InvoiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private BillingAccountService billingAccountService;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    @DisplayName("Geçerli hesap ID'siyle fatura oluşturulmalı")
    void createInvoice_gecerliHesap_faturaOlusturur() {
        BillingAccount account = new BillingAccount();
        account.setId(1L);

        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceNumber("INV-2026-001");
        request.setInvoiceDate(LocalDate.of(2026, 8, 28));
        request.setAmount(new BigDecimal("149.90"));
        request.setPaid(false);

        Invoice savedInvoice = new Invoice();
        savedInvoice.setId(1L);
        savedInvoice.setInvoiceNumber("INV-2026-001");
        savedInvoice.setInvoiceDate(LocalDate.of(2026, 8, 28));
        savedInvoice.setAmount(new BigDecimal("149.90"));
        savedInvoice.setPaid(false);
        savedInvoice.setBillingAccount(account);

        when(billingAccountService.getBillingAccountEntityById(1L)).thenReturn(account);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        InvoiceResponse response = invoiceService.createInvoice(1L, request);

        assertEquals("INV-2026-001", response.getInvoiceNumber());
        assertEquals(new BigDecimal("149.90"), response.getAmount());
        assertFalse(response.isPaid());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    @DisplayName("Olmayan hesap ID'siyle fatura oluşturulmaya çalışılırsa exception fırlatmalı")
    void createInvoice_olmayanHesap_exceptionFirlatir() {
        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceNumber("INV-2026-001");

        when(billingAccountService.getBillingAccountEntityById(999L))
                .thenThrow(new ResourceNotFoundException("Hesap bulunamadı: 999"));

        assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.createInvoice(999L, request);
        });

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    @DisplayName("Fatura ödendi olarak güncellendiğinde paid alanı true olmalı")
    void updateInvoice_odendiOlarakIsaretlendiginde_paidTrueOlur() {
        BillingAccount account = new BillingAccount();
        account.setId(1L);

        Invoice existing = new Invoice();
        existing.setId(1L);
        existing.setInvoiceNumber("INV-2026-001");
        existing.setInvoiceDate(LocalDate.of(2026, 8, 28));
        existing.setAmount(new BigDecimal("149.90"));
        existing.setPaid(false);
        existing.setBillingAccount(account);

        InvoiceRequest updateRequest = new InvoiceRequest();
        updateRequest.setInvoiceNumber("INV-2026-001");
        updateRequest.setInvoiceDate(LocalDate.of(2026, 8, 28));
        updateRequest.setAmount(new BigDecimal("149.90"));
        updateRequest.setPaid(true);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(existing);

        InvoiceResponse response = invoiceService.updateInvoice(1L, updateRequest);

        assertTrue(response.isPaid());
    }

    @Test
    @DisplayName("Olmayan fatura ID'si sorgulandığında exception fırlatmalı")
    void getInvoiceById_faturaYoksa_exceptionFirlatir() {
        when(invoiceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.getInvoiceById(999L);
        });
    }

    @Test
    @DisplayName("Fatura silindiğinde repository'nin deleteById metodu çağrılmalı")
    void deleteInvoice_gecerliId_deleteByIdCagirir() {
        invoiceService.deleteInvoice(1L);

        verify(invoiceRepository, times(1)).deleteById(1L);
    }
}