package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.InvoiceRequest;
import com.example.musteri_faturalama_sistemi.dto.InvoiceResponse;
import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.Invoice;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BillingAccountService billingAccountService;

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public InvoiceResponse getInvoiceById(Long id) {
        return toResponse(getInvoiceEntityById(id));
    }

    public InvoiceResponse createInvoice(Long billingAccountId, InvoiceRequest request) {
        BillingAccount billingAccount = billingAccountService.getBillingAccountEntityById(billingAccountId);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setAmount(request.getAmount());
        invoice.setPaid(request.isPaid());
        invoice.setBillingAccount(billingAccount);

        Invoice saved = invoiceRepository.save(invoice);
        return toResponse(saved);
    }

    public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
        Invoice existing = getInvoiceEntityById(id);
        existing.setInvoiceNumber(request.getInvoiceNumber());
        existing.setInvoiceDate(request.getInvoiceDate());
        existing.setAmount(request.getAmount());
        existing.setPaid(request.isPaid());
        Invoice updated = invoiceRepository.save(existing);
        return toResponse(updated);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    // --- Yardımcı metotlar ---

    public Invoice getInvoiceEntityById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura bulunamadı: " + id));
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getInvoiceDate(),
                invoice.getAmount(),
                invoice.isPaid(),
                invoice.getBillingAccount().getId()
        );
    }
}