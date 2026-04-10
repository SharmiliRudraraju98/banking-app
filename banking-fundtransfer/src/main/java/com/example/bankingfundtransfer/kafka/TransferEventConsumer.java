package com.example.bankingfundtransfer.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransferEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransferEventConsumer.class);

    @KafkaListener(topics = "transfer-events", groupId = "banking-group")
    public void consumeTransferEvent(TransferEvent event) {
        log.info("Received transfer event from Kafka: {}", event);
        log.info("Transfer | FROM: {} | TO: {} | AMOUNT: {} | STATUS: {}",
                event.getOriginAccount(),
                event.getDestinationAccount(),
                event.getAmount(),
                event.getStatus());
    }
}