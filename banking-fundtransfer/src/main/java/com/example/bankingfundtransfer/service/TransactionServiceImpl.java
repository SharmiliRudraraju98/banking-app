package com.example.bankingfundtransfer.service;

import com.example.bankingfundtransfer.dto.AccountResponseDTO;
import com.example.bankingfundtransfer.dto.TransactionRequestDTO;
import com.example.bankingfundtransfer.dto.TransactionResponseDTO;
import com.example.bankingfundtransfer.entity.Account;
import com.example.bankingfundtransfer.entity.Customer;
import com.example.bankingfundtransfer.entity.Transaction;
import com.example.bankingfundtransfer.repository.AccountRepository;
import com.example.bankingfundtransfer.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public  TransactionServiceImpl(TransactionRepository transactionRepository,AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;

    }


    @Transactional
    @Override
    public TransactionResponseDTO transferFunds(TransactionRequestDTO dto){
        Optional<Transaction> existing = transactionRepository.findByIdempotentKey(dto.getIdempotentKey());
        if(existing.isPresent()) {
            return mapToResponseDTO(existing.get());
        }

        Account originAccount = accountRepository.findByAccountNumber(dto.getOriginAccountNumber());
        if(originAccount == null) {
            throw new RuntimeException("Origin account not found");
        }
        Account destinationAccount = accountRepository.findByAccountNumber(dto.getDestinationAccountNumber());
        if(destinationAccount == null) {
            throw new RuntimeException("Destination account not found");
        }

        if(originAccount.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }


        originAccount.setBalance(originAccount.getBalance().subtract(dto.getAmount()));


        destinationAccount.setBalance(destinationAccount.getBalance().add(dto.getAmount()));


        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction(
                originAccount,
                destinationAccount,
                dto.getAmount(),
                dto.getComment(),
                Transaction.TransactionStatus.SUCCESS,
                LocalDateTime.now(),
                dto.getIdempotentKey()
        );

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponseDTO(saved);
    }

    @Override
    public TransactionResponseDTO getTransactionbyId(Long transactionId){
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));
        return mapToResponseDTO(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        List<Transaction> sent = transactionRepository.findByOriginAccountId(accountId);
        List<Transaction> received = transactionRepository.findByDestinationAccountId(accountId);

        sent.addAll(received); // combine both lists

        return sent.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }

    private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setDestinationAccountNumber(transaction.getDestinationAccount().getAccountNumber());
        response.setOriginAccountNumber(transaction.getOriginAccount().getAccountNumber());
        response.setStatus(transaction.getStatus().name());
        response.setComment(transaction.getComment());
        response.setDate(transaction.getDate());
        response.setIdempotentKey(transaction.getIdempotentKey());
        return response;
    }

}
