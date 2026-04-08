package com.example.bankingfundtransfer.service;
import com.example.bankingfundtransfer.dto.AccountRequestDTO;
import com.example.bankingfundtransfer.dto.AccountResponseDTO;


import java.util.List;

public interface AccountService {

    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO);
    AccountResponseDTO getAccountById(Long id);
    List<AccountResponseDTO> getAccountsByCustomerId(Long customerId);
}
