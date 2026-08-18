package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import com.example.musteri_faturalama_sistemi.entity.Invoice;
import com.example.musteri_faturalama_sistemi.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BillingAccountService billingAccountService;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı: " + id));
    }

    public Invoice createInvoice(Long billingAccountId, Invoice invoice) {
        BillingAccount billingAccount = billingAccountService.getBillingAccountById(billingAccountId);
        invoice.setBillingAccount(billingAccount);
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        Invoice existing = getInvoiceById(id);
        existing.setInvoiceNumber(updatedInvoice.getInvoiceNumber());
        existing.setInvoiceDate(updatedInvoice.getInvoiceDate());
        existing.setAmount(updatedInvoice.getAmount());
        existing.setPaid(updatedInvoice.isPaid());
        return invoiceRepository.save(existing);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}