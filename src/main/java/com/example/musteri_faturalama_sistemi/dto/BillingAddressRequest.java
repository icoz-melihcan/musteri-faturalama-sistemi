// dto/BillingAddressRequest.java
package com.example.musteri_faturalama_sistemi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddressRequest {
    private String city;
    private String district;
    private String fullAddress;
    private String postalCode;
}