package com.example.musteri_faturalama_sistemi.controller;

import com.example.musteri_faturalama_sistemi.entity.Invoice;
import com.example.musteri_faturalama_sistemi.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/api/billing-accounts/{billingAccountId}/invoices")
    public Invoice createInvoice(@PathVariable Long billingAccountId,
                                 @RequestBody Invoice invoice) {
        return invoiceService.createInvoice(billingAccountId, invoice);
    }

    @GetMapping("/api/invoices")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/api/invoices/{id}")
    public Invoice getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PutMapping("/api/invoices/{id}")
    public Invoice updateInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        return invoiceService.updateInvoice(id, invoice);
    }

    @DeleteMapping("/api/invoices/{id}")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }
}