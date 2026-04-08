package com.example.bankingfundtransfer.service;

import com.example.bankingfundtransfer.dto.AccountRequestDTO;
import com.example.bankingfundtransfer.dto.AccountResponseDTO;
import com.example.bankingfundtransfer.dto.CustomerResponseDTO;
import com.example.bankingfundtransfer.entity.Account;
import com.example.bankingfundtransfer.entity.Customer;
import com.example.bankingfundtransfer.repository.AccountRepository;
import com.example.bankingfundtransfer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;

    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO dto){
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = new Account(
                dto.getAccountNumber(),
                dto.getBalance(),
                dto.getAccountType(),
                "ACTIVE",               // server sets this
                LocalDateTime.now(),    // server sets this
                customer);

        Account saved =  accountRepository.save(account);
        return mapToResponseDTO(saved);

    }
    @Override
    public AccountResponseDTO getAccountById(Long accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        return mapToResponseDTO(account);
    }

    @Override
    public List<AccountResponseDTO> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private AccountResponseDTO mapToResponseDTO(Account account) {
        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        response.setAccountType(account.getAccountType());
        response.setCreationDate(account.getCreationDate());
        response.setStatus(account.getStatus());
        response.setCustomerId(account.getCustomer().getId());
        return response;
    }


}
