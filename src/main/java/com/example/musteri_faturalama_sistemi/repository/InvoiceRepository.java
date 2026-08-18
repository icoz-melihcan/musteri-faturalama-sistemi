package com.example.musteri_faturalama_sistemi.repository;

import com.example.musteri_faturalama_sistemi.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}