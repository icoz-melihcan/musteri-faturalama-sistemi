package com.example.musteri_faturalama_sistemi.service;

import com.example.musteri_faturalama_sistemi.dto.CustomerRequest;
import com.example.musteri_faturalama_sistemi.dto.CustomerResponse;
import com.example.musteri_faturalama_sistemi.entity.Customer;
import com.example.musteri_faturalama_sistemi.exception.ResourceNotFoundException;
import com.example.musteri_faturalama_sistemi.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getCustomerById_musteriVarsa_dogruBilgiyiDondurur() {
        Customer sahteCustomer = new Customer();
        sahteCustomer.setId(1L);
        sahteCustomer.setFirstName("Ahmet");
        sahteCustomer.setLastName("Yılmaz");
        sahteCustomer.setEmail("ahmet@example.com");
        sahteCustomer.setPhoneNumber("05551234567");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sahteCustomer));

        CustomerResponse response = customerService.getCustomerById(1L);

        assertEquals("Ahmet", response.getFirstName());
        assertEquals("ahmet@example.com", response.getEmail());
    }

    @Test
    void getCustomerById_musteriYoksa_exceptionFirlatir() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(999L);
        });
    }
    @Test
    void createCustomer_gecerliVeriyle_musteriOlusturur() {
        // ARRANGE: request hazırlıyoruz
        CustomerRequest request = new CustomerRequest();
        request.setFirstName("Mehmet");
        request.setLastName("Demir");
        request.setEmail("mehmet@example.com");
        request.setPhoneNumber("05559876543");

        // save() çağrıldığında, id'si atanmış bir Customer dönmesini simüle ediyoruz
        Customer kaydedilenCustomer = new Customer();
        kaydedilenCustomer.setId(1L);
        kaydedilenCustomer.setFirstName("Mehmet");
        kaydedilenCustomer.setLastName("Demir");
        kaydedilenCustomer.setEmail("mehmet@example.com");
        kaydedilenCustomer.setPhoneNumber("05559876543");

        when(customerRepository.save(any(Customer.class))).thenReturn(kaydedilenCustomer);

        // ACT
        CustomerResponse response = customerService.createCustomer(request);

        // ASSERT: dönen response doğru mu?
        assertEquals(1L, response.getId());
        assertEquals("Mehmet", response.getFirstName());
        assertEquals("mehmet@example.com", response.getEmail());

        // ASSERT: save() gerçekten çağrıldı mı?
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    @Test
    @DisplayName("Tüm müşteriler listelendiğinde doğru sayıda sonuç dönmeli")
    void getAllCustomers_musterilerVarsa_listeyiDondurur() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("Ahmet");
        customer1.setLastName("Yılmaz");
        customer1.setEmail("ahmet@example.com");
        customer1.setPhoneNumber("05551234567");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Ayşe");
        customer2.setLastName("Kara");
        customer2.setEmail("ayse@example.com");
        customer2.setPhoneNumber("05559876543");

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<CustomerResponse> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("Ahmet", result.get(0).getFirstName());
        assertEquals("Ayşe", result.get(1).getFirstName());
    }

    @Test
    @DisplayName("Hiç müşteri yoksa boş liste dönmeli")
    void getAllCustomers_musteriYoksa_bosListeDondurur() {
        when(customerRepository.findAll()).thenReturn(List.of());

        List<CustomerResponse> result = customerService.getAllCustomers();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Var olan müşteri güncellendiğinde yeni bilgiler dönmeli")
    void updateCustomer_musteriVarsa_bilgileriGunceller() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setFirstName("Ahmet");
        existing.setLastName("Yılmaz");
        existing.setEmail("eski@example.com");
        existing.setPhoneNumber("05551111111");

        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setFirstName("Ahmet");
        updateRequest.setLastName("Yılmaz");
        updateRequest.setEmail("yeni@example.com");
        updateRequest.setPhoneNumber("05552222222");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenReturn(existing);

        CustomerResponse response = customerService.updateCustomer(1L, updateRequest);

        assertEquals("yeni@example.com", response.getEmail());
        assertEquals("05552222222", response.getPhoneNumber());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Olmayan müşteri güncellenmeye çalışılırsa exception fırlatmalı")
    void updateCustomer_musteriYoksa_exceptionFirlatir() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setFirstName("Test");

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateCustomer(999L, updateRequest);
        });

        // save() hiç çağrılmamalı, çünkü müşteri bulunamadı
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Müşteri silindiğinde repository'nin deleteById metodu çağrılmalı")
    void deleteCustomer_gecerliId_deleteByIdCagirir() {
        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }
}