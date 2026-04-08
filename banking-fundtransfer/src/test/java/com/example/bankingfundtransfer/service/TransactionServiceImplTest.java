package com.example.bankingfundtransfer.service;

import com.example.bankingfundtransfer.dto.CustomerRequestDTO;
import com.example.bankingfundtransfer.dto.TransactionRequestDTO;
import com.example.bankingfundtransfer.dto.TransactionResponseDTO;
import com.example.bankingfundtransfer.entity.Account;
import com.example.bankingfundtransfer.entity.Customer;
import com.example.bankingfundtransfer.entity.Transaction;
import com.example.bankingfundtransfer.repository.AccountRepository;
import com.example.bankingfundtransfer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private TransactionRequestDTO transactionRequestDTO;
    private Account originAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        // Create two accounts
        originAccount = new Account();
        originAccount.setId(1L);
        originAccount.setAccountNumber("ACC001");
        originAccount.setBalance(new BigDecimal("5000"));
        originAccount.setStatus("ACTIVE");
        originAccount.setAccountType("SAVINGS");

        destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setAccountNumber("ACC002");
        destinationAccount.setBalance(new BigDecimal("2000"));
        destinationAccount.setStatus("ACTIVE");
        destinationAccount.setAccountType("SAVINGS");

        // Create request DTO
        transactionRequestDTO = new TransactionRequestDTO();
        transactionRequestDTO.setOriginAccountNumber("ACC001");
        transactionRequestDTO.setDestinationAccountNumber("ACC002");
        transactionRequestDTO.setAmount(new BigDecimal("1000"));
        transactionRequestDTO.setComment("rent payment");
        transactionRequestDTO.setIdempotentKey("txn-001");
    }

    @Test
    void transferFunds_Success() {
        // Step 1 - no duplicate transaction
        when(transactionRepository.findByIdempotentKey("txn-001"))
                .thenReturn(Optional.empty());

        // Step 2 - mock finding both accounts
        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(originAccount);
        when(accountRepository.findByAccountNumber("ACC002"))
                .thenReturn(destinationAccount);

        // Step 3 - mock saving accounts
        when(accountRepository.save(any(Account.class)))
                .thenReturn(originAccount);

        // Step 4 - mock saving transaction
        Transaction savedTransaction = new Transaction(
                originAccount,
                destinationAccount,
                new BigDecimal("1000"),
                "rent payment",
                Transaction.TransactionStatus.SUCCESS,
                LocalDateTime.now(),
                "txn-001"
        );
        savedTransaction.setId(1L);
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(savedTransaction);

        // Step 5 - call the service
        TransactionResponseDTO response = transactionService.transferFunds(transactionRequestDTO);

        // Step 6 - verify
        assertNotNull(response);
        assertEquals("ACC001", response.getOriginAccountNumber());
        assertEquals("ACC002", response.getDestinationAccountNumber());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(new BigDecimal("4000"), originAccount.getBalance());
        assertEquals(new BigDecimal("3000"), destinationAccount.getBalance());
    }

    @Test
    void transferFunds_InsufficientBalance() {
        // Set balance lower than transfer amount
        originAccount.setBalance(new BigDecimal("500"));

        when(transactionRepository.findByIdempotentKey("txn-001"))
                .thenReturn(Optional.empty());

        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(originAccount);
        when(accountRepository.findByAccountNumber("ACC002"))
                .thenReturn(destinationAccount);

        // Expect exception
        assertThrows(RuntimeException.class, () -> {
            transactionService.transferFunds(transactionRequestDTO);
        });
    }

        @Test
        void transferFunds_DuplicateIdempotentKey() {
            // Existing transaction already in DB
            Transaction existingTransaction = new Transaction(
                    originAccount,
                    destinationAccount,
                    new BigDecimal("1000"),
                    "rent payment",
                    Transaction.TransactionStatus.SUCCESS,
                    LocalDateTime.now(),
                    "txn-001"
            );
            existingTransaction.setId(1L);

            // Fake repo says this key already exists
            when(transactionRepository.findByIdempotentKey("txn-001"))
                    .thenReturn(Optional.of(existingTransaction));

            // Call service
            TransactionResponseDTO response = transactionService.transferFunds(transactionRequestDTO);

            // Should return existing transaction, not process again
            assertNotNull(response);
            assertEquals("txn-001", response.getIdempotentKey());
            assertEquals("SUCCESS", response.getStatus());

            // Verify accounts were NEVER saved (no double transfer!)
            verify(accountRepository, never()).save(any(Account.class));
        }


}
