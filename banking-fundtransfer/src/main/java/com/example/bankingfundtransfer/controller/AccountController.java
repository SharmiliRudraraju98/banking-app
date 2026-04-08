package com.example.bankingfundtransfer.controller;


import com.example.bankingfundtransfer.dto.AccountResponseDTO;
import com.example.bankingfundtransfer.dto.AccountRequestDTO;
import com.example.bankingfundtransfer.dto.CustomerRequestDTO;
import com.example.bankingfundtransfer.dto.CustomerResponseDTO;
import com.example.bankingfundtransfer.service.AccountService;
//import jakarta.validation.Valid;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO dto) {
        AccountResponseDTO response = accountService.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsByCustomerId(@PathVariable Long customerId){
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
    }

}
