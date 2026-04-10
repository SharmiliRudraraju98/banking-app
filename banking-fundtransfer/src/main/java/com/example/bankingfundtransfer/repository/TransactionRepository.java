package com.example.bankingfundtransfer.repository;

import com.example.bankingfundtransfer.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByIdempotentKey(String idempotentKey);

    List<Transaction> findByOriginAccountId(Long accountId);
    List<Transaction> findByDestinationAccountId(Long accountId);


    List<Transaction> findTop10ByOriginAccountIdOrDestinationAccountIdOrderByDateDesc(Long originId, Long destinationId);
}