package com.example.bankingfundtransfer.service;


import com.example.bankingfundtransfer.dto.CustomerRequestDTO;
import com.example.bankingfundtransfer.dto.CustomerResponseDTO;
import com.example.bankingfundtransfer.entity.Customer;
import com.example.bankingfundtransfer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequestDTO requestDTO;


    @BeforeEach
    void setUp() {
        customer = new Customer("John Doe", "john@example.com", "123 Main St", "Apt 4", "USA");
        customer.setId(1L); // won't compile yet - we need a setId method!

        requestDTO = new CustomerRequestDTO();
        requestDTO.setName("John Doe");
        requestDTO.setEmail("john@example.com");
        requestDTO.setAddressLine1("123 Main St");
        requestDTO.setAddressLine2("Apt 4");
        requestDTO.setCountry("USA");
    }

    @Test
    void createCustomer_Success() {
        // Step 1 - TELL the fake repo what to return
        when(customerRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.empty()); // email doesn't exist yet

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer); // pretend save returns our customer

        // Step 2 - call the real service
        CustomerResponseDTO response = customerService.createCustomer(requestDTO);
        System.out.println(response.getName());
        // Step 3 - verify the response is correct
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void getCustomerById_Success() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        CustomerResponseDTO response = customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
    }

    @Test
    void getCustomerById_Failed(){

        when(customerRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(999L); // ← must match the mocked id!
        });

    }
}
