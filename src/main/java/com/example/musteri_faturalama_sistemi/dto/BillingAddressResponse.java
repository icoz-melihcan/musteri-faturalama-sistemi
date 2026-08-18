package com.example.musteri_faturalama_sistemi.dto;

public class BillingAddressResponse {
    private Long id;
    private String city;
    private String district;
    private String fullAddress;
    private String postalCode;
    private Long billingAccountId;

    public BillingAddressResponse() {
    }

    public BillingAddressResponse(Long id, String city, String district, String fullAddress,
                                  String postalCode, Long billingAccountId) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.fullAddress = fullAddress;
        this.postalCode = postalCode;
        this.billingAccountId = billingAccountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getBillingAccountId() {
        return billingAccountId;
    }

    public void setBillingAccountId(Long billingAccountId) {
        this.billingAccountId = billingAccountId;
    }
}