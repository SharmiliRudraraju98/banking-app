package com.example.bankingfundtransfer.controller;

import com.example.bankingfundtransfer.dto.AccountRequestDTO;
import com.example.bankingfundtransfer.dto.AccountResponseDTO;
import com.example.bankingfundtransfer.dto.TransactionRequestDTO;
import com.example.bankingfundtransfer.dto.TransactionResponseDTO;
import com.example.bankingfundtransfer.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> transferFunds(@RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO response = transactionService.transferFunds(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionbyId(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionbyId(id));
    }

    @GetMapping("/accounts/{accountId}")

    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }
    

}
