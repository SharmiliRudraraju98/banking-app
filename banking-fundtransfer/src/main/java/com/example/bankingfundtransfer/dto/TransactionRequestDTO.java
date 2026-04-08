package com.example.bankingfundtransfer.dto;

import java.math.BigDecimal;

public class TransactionRequestDTO {
    private String originAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private String comment;
    private String idempotentKey;

    public TransactionRequestDTO() {}

    public String getOriginAccountNumber() { return originAccountNumber; }
    public void setOriginAccountNumber(String originAccountNumber) { this.originAccountNumber = originAccountNumber; }

    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getIdempotentKey() { return idempotentKey; }
    public void setIdempotentKey(String idempotentKey) { this.idempotentKey = idempotentKey; }
}