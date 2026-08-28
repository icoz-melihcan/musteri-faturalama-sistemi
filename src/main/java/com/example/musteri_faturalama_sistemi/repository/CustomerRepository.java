package com.example.musteri_faturalama_sistemi.repository;

import com.example.musteri_faturalama_sistemi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
// <Customer, Long>
//< hangi sınıfla işlem yapacağın, o sınıfta primary key olarak işaretlediğimizin typeni yazıyorz>
// yani sınıf adı Customer Long kısmı ise @id yazdığımzı yer long id yazmıştık.
