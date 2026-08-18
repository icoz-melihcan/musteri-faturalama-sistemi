package com.example.musteri_faturalama_sistemi.dto;

public class BillingAccountResponse {
    private Long id;
    private String accountNumber;
    private Long customerId;

    public BillingAccountResponse() {
    }

    public BillingAccountResponse(Long id, String accountNumber, Long customerId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}