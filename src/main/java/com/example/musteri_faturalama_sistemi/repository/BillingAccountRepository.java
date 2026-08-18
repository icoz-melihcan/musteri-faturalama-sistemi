package com.example.musteri_faturalama_sistemi.repository;

import com.example.musteri_faturalama_sistemi.entity.BillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingAccountRepository extends JpaRepository<BillingAccount, Long> {
}