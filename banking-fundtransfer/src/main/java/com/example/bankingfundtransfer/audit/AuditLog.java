package com.example.bankingfundtransfer.audit;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    private String eventType;       // TRANSFER_INITIATED, TRANSFER_SUCCESS, etc.
    private String originAccount;
    private String destinationAccount;
    private java.math.BigDecimal amount;
    private String idempotentKey;
    private String message;
    private LocalDateTime timestamp;

    // Constructor
    public AuditLog(String eventType, String originAccount, String destinationAccount,
                    java.math.BigDecimal amount, String idempotentKey,
                    String message, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.idempotentKey = idempotentKey;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getEventType() { return eventType; }
    public String getOriginAccount() { return originAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public java.math.BigDecimal getAmount() { return amount; }
    public String getIdempotentKey() { return idempotentKey; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}