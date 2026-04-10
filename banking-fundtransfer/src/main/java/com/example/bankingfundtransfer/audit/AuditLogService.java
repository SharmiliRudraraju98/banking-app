package com.example.bankingfundtransfer.audit;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String eventType, String originAccount, String destinationAccount,
                    BigDecimal amount, String idempotentKey, String message) {
        AuditLog auditLog = new AuditLog(
                eventType, originAccount, destinationAccount,
                amount, idempotentKey, message, LocalDateTime.now()
        );
        auditLogRepository.save(auditLog);
    }
}