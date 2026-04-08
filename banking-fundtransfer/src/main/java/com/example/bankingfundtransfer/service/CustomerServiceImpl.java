package com.example.bankingfundtransfer.service;

import com.example.bankingfundtransfer.dto.CustomerRequestDTO;
import com.example.bankingfundtransfer.dto.CustomerResponseDTO;
import com.example.bankingfundtransfer.entity.Customer;
import com.example.bankingfundtransfer.repository.CustomerRepository;
import com.example.bankingfundtransfer.service.CustomerService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        // Check if email already exists
        if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Customer with this email already exists");
        }
        // Map DTO → Entity
        Customer customer = new Customer(
                dto.getName(),
                dto.getEmail(),
                dto.getAddressLine1(),
                dto.getAddressLine2(),
                dto.getCountry()
        );
        // Save to DB
        Customer saved = customerRepository.save(customer);
        // Map Entity → ResponseDTO
        return mapToResponseDTO(saved);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToResponseDTO(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method — reusable mapping
    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setAddressLine1(customer.getAddressLine1());
        response.setAddressLine2(customer.getAddressLine2());
        response.setCountry(customer.getCountry());
        return response;
    }
}