package com.example.bankingfundtransfer.service;

import com.example.bankingfundtransfer.audit.AuditLogService;
import com.example.bankingfundtransfer.dto.AccountResponseDTO;
import com.example.bankingfundtransfer.dto.TransactionRequestDTO;
import com.example.bankingfundtransfer.dto.TransactionResponseDTO;
import com.example.bankingfundtransfer.entity.Account;
import com.example.bankingfundtransfer.entity.Customer;
import com.example.bankingfundtransfer.entity.Transaction;
import com.example.bankingfundtransfer.kafka.TransferEvent;
import com.example.bankingfundtransfer.kafka.TransferEventProducer;
import com.example.bankingfundtransfer.repository.AccountRepository;
import com.example.bankingfundtransfer.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionServiceImpl implements TransactionService{

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

//    public  TransactionServiceImpl(TransactionRepository transactionRepository,AccountRepository accountRepository){
//        this.transactionRepository = transactionRepository;
//        this.accountRepository = accountRepository;
//
//    }
    private final AuditLogService auditLogService;

    private final TransferEventProducer transferEventProducer;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  AuditLogService auditLogService,
                                  TransferEventProducer transferEventProducer) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.auditLogService = auditLogService;
        this.transferEventProducer = transferEventProducer;
    }


    @Transactional
    @Override
    public TransactionResponseDTO transferFunds(TransactionRequestDTO dto){


//        log.info("AUDIT: Transfer initiated | FROM: {} | TO: {} | AMOUNT: {} | KEY: {}",
//                dto.getOriginAccountNumber(),
//                dto.getDestinationAccountNumber(),
//                dto.getAmount(),
//                dto.getIdempotentKey());

        auditLogService.log("TRANSFER_INITIATED",
                dto.getOriginAccountNumber(), dto.getDestinationAccountNumber(),
                dto.getAmount(), dto.getIdempotentKey(), "Transfer initiated");

        Optional<Transaction> existing = transactionRepository.findByIdempotentKey(dto.getIdempotentKey());
        if(existing.isPresent()) {
//            log.warn("AUDIT: Duplicate transfer detected | KEY: {}", dto.getIdempotentKey());

            auditLogService.log("DUPLICATE_KEY",
                    dto.getOriginAccountNumber(), dto.getDestinationAccountNumber(),
                    dto.getAmount(), dto.getIdempotentKey(), "Duplicate transfer detected");

            return mapToResponseDTO(existing.get());
        }

        Account originAccount = accountRepository.findByAccountNumber(dto.getOriginAccountNumber());
        if(originAccount == null) {
//            log.error("AUDIT: Origin account not found | ACCOUNT: {}", dto.getOriginAccountNumber());

            auditLogService.log("ORIGIN_NOT_FOUND",
                    dto.getOriginAccountNumber(), null,
                    dto.getAmount(), dto.getIdempotentKey(), "Origin account not found");


            throw new RuntimeException("Origin account not found");
        }
        Account destinationAccount = accountRepository.findByAccountNumber(dto.getDestinationAccountNumber());
        if(destinationAccount == null) {
//            log.error("AUDIT: Destination account not found | ACCOUNT: {}", dto.getDestinationAccountNumber());

            auditLogService.log("DESTINATION_NOT_FOUND",
                    dto.getOriginAccountNumber(), dto.getDestinationAccountNumber(),
                    dto.getAmount(), dto.getIdempotentKey(), "Destination account not found");

            throw new RuntimeException("Destination account not found");
        }

        if(originAccount.getBalance().compareTo(dto.getAmount()) < 0) {
//            log.warn("AUDIT: Insufficient balance | ACCOUNT: {} | BALANCE: {} | REQUESTED: {}",
//                    dto.getOriginAccountNumber(),
//                    originAccount.getBalance(),
//                    dto.getAmount());

            auditLogService.log("INSUFFICIENT_BALANCE",
                    dto.getOriginAccountNumber(), dto.getDestinationAccountNumber(),
                    dto.getAmount(), dto.getIdempotentKey(), "Insufficient balance");




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

//        log.info("AUDIT: Transfer completed successfully | ID: {} | FROM: {} | TO: {} | AMOUNT: {}",
//                saved.getId(),
//                dto.getOriginAccountNumber(),
//                dto.getDestinationAccountNumber(),
//                dto.getAmount());

        auditLogService.log("TRANSFER_SUCCESS",
                dto.getOriginAccountNumber(), dto.getDestinationAccountNumber(),
                dto.getAmount(), dto.getIdempotentKey(), "Transfer completed successfully | ID: " + saved.getId());



        // Publish to Kafka
        TransferEvent event = new TransferEvent(
                dto.getOriginAccountNumber(),
                dto.getDestinationAccountNumber(),
                dto.getAmount(),
                dto.getIdempotentKey(),
                "SUCCESS",
                LocalDateTime.now()
        );
        transferEventProducer.publishTransferEvent(event);
        return mapToResponseDTO(saved);
    }

    @Override
    public TransactionResponseDTO getTransactionbyId(Long transactionId){
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));
        return mapToResponseDTO(transaction);
    }

//    @Override
//    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
//        List<Transaction> sent = transactionRepository.findByOriginAccountId(accountId);
//        List<Transaction> received = transactionRepository.findByDestinationAccountId(accountId);
//
//        sent.addAll(received); // combine both lists
//
//        return sent.stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//
//    }


    @Override
    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        return transactionRepository
                .findTop10ByOriginAccountIdOrDestinationAccountIdOrderByDateDesc(accountId, accountId)
                .stream()
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
