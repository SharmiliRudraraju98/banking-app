package com.example.bankingfundtransfer.service;

import com.example.bankingfundtransfer.dto.CustomerRequestDTO;
import com.example.bankingfundtransfer.dto.CustomerResponseDTO;
import java.util.List;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO getCustomerById(Long id);
    List<CustomerResponseDTO> getAllCustomers();
}