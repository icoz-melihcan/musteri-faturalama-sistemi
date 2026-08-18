package com.example.musteri_faturalama_sistemi.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "billing_addresses")
public class BillingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq", sequenceName = "address_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String fullAddress;

    private String postalCode;

    @OneToOne
    @JoinColumn(name = "billing_account_id", nullable = false, unique = true)
    @JsonBackReference("account-address")
    private BillingAccount billingAccount;

    public BillingAddress() {
    }

    public BillingAddress(String city, String district, String fullAddress, String postalCode, BillingAccount billingAccount) {
        this.city = city;
        this.district = district;
        this.fullAddress = fullAddress;
        this.postalCode = postalCode;
        this.billingAccount = billingAccount;
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

    public BillingAccount getBillingAccount() {
        return billingAccount;
    }

    public void setBillingAccount(BillingAccount billingAccount) {
        this.billingAccount = billingAccount;
    }
}