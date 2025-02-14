package com.pdp.transaction.service.kafka.producer;

import com.pdp.transaction.persistent.postgres.entity.TransactionsEntity;
import com.pdp.transaction.service.kafka.dto.TransactionPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionMessageProducer {

    private static final String TOPIC = "transaction-topic";

    private final KafkaTemplate<String, TransactionPayload> kafkaTemplate;

    public TransactionMessageProducer(KafkaTemplate<String, TransactionPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransaction(TransactionsEntity entity) {
        var transactionPayload = TransactionPayload.newBuilder()
                .setTransactionId(String.valueOf(entity.getTransactionId()))
                .setStatus(entity.getStatus())
                .setUserId(entity.getUserId())
                .build();

        kafkaTemplate.send(TOPIC, String.valueOf(entity.getTransactionId()), transactionPayload);
        log.info("Transaction message sent: {}", transactionPayload);
    }
}
