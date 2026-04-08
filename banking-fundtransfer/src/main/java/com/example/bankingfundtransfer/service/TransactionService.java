package com.example.bankingfundtransfer.service;
import com.example.bankingfundtransfer.dto.TransactionRequestDTO;
import com.example.bankingfundtransfer.dto.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {
           TransactionResponseDTO transferFunds(TransactionRequestDTO dto);
           TransactionResponseDTO getTransactionbyId(Long id);
           List<TransactionResponseDTO> getTransactionsByAccountId(Long id);
}

