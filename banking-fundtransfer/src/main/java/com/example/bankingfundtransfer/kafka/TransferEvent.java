package com.example.bankingfundtransfer.kafka;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferEvent {

    private String originAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private String idempotentKey;
    private String status;
    private LocalDateTime timestamp;

    // Default constructor (required for Kafka deserialization)
    public TransferEvent() {}

    public TransferEvent(String originAccount, String destinationAccount,
                         BigDecimal amount, String idempotentKey,
                         String status, LocalDateTime timestamp) {
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.idempotentKey = idempotentKey;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public String getOriginAccount() { return originAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getIdempotentKey() { return idempotentKey; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setOriginAccount(String originAccount) { this.originAccount = originAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setIdempotentKey(String idempotentKey) { this.idempotentKey = idempotentKey; }
    public void setStatus(String status) { this.status = status; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "TransferEvent{" +
                "originAccount='" + originAccount + '\'' +
                ", destinationAccount='" + destinationAccount + '\'' +
                ", amount=" + amount +
                ", idempotentKey='" + idempotentKey + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}