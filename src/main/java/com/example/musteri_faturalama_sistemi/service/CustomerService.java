package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.CustomerRequest;
import com.example.musteri_faturalama_sistemi.dto.CustomerResponse;
import com.example.musteri_faturalama_sistemi.entity.Customer;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    //IoC mantığı nesneleri spring yaratır.
    @Autowired  // DI oluyor bu kısım. Bana bunu getir demek oluyor.
    private CustomerRepository customerRepository;

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = getCustomerEntityById(id);
        return toResponse(customer);
        // Bu kısım controller katmanı için çünkü controllerin entitiyle işi yok Ona DTO lazm
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer existing = getCustomerEntityById(id);
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());
        existing.setPhoneNumber(request.getPhoneNumber());
        Customer updated = customerRepository.save(existing);
        return toResponse(updated);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // --- Yardımcı metotlar ---

    // Bu kısım ise diğer servicele için çünkü servisler arasında entity kullanılıyor.
    public Customer getCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Müşteri bulunamadı: " + id));
    }

    //Bu kısım ise customer entityleri tek tek okuyup dışarıya gösterilecek sade bir nesne üretiyor. DTO
    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }
}