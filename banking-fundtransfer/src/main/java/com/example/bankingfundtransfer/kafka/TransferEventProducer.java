package com.example.bankingfundtransfer.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TransferEventProducer.class);
    private static final String TOPIC = "transfer-events";

    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;

    public TransferEventProducer(KafkaTemplate<String, TransferEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTransferEvent(TransferEvent event) {
        log.info("Publishing transfer event to Kafka: {}", event);
        kafkaTemplate.send(TOPIC, event.getIdempotentKey(), event);
    }
}