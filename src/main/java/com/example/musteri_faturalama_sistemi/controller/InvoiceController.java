package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.dto.InvoiceRequest;
import com.example.musteri_faturalama_sistemi.dto.InvoiceResponse;
import com.example.musteri_faturalama_sistemi.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/api/billing-accounts/{billingAccountId}/invoices")
    public InvoiceResponse createInvoice(@PathVariable Long billingAccountId,
                                         @RequestBody InvoiceRequest request) {
        return invoiceService.createInvoice(billingAccountId, request);
    }

    @GetMapping("/api/invoices")
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/api/invoices/{id}")
    public InvoiceResponse getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PutMapping("/api/invoices/{id}")
    public InvoiceResponse updateInvoice(@PathVariable Long id, @RequestBody InvoiceRequest request) {
        return invoiceService.updateInvoice(id, request);
    }

    @DeleteMapping("/api/invoices/{id}")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }
}