package com.pdp.transaction.service.kafka;

import com.pdp.transaction.service.SenderService;
import com.pdp.transaction.service.kafka.dto.TransactionPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumer {

    private final SenderService senderService;

    @KafkaListener(topics = "transaction-topic")
    public void processTransaction(ConsumerRecord<String, TransactionPayload> message) {
        var payload = message.value();
        log.info("Received Transaction: {}", payload);
        senderService.sendMail(payload);
    }
}
