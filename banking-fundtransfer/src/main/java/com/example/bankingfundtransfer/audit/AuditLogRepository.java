package com.example.bankingfundtransfer.audit;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByIdempotentKey(String idempotentKey);
    List<AuditLog> findByOriginAccount(String originAccount);
    List<AuditLog> findByEventType(String eventType);
}