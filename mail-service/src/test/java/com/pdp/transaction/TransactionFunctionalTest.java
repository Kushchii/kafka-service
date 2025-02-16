package com.pdp.transaction;

import com.pdp.transaction.api.request.MailRequest;
import com.pdp.transaction.api.response.MailResponse;
import com.pdp.transaction.service.kafka.dto.TransactionPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static com.pdp.transaction.BaseTest.random;

@ExtendWith(OutputCaptureExtension.class)
class TransactionFunctionalTest extends BaseFunctionalTest {

    private static final String USER_ID = "1e0d2473-1396-4d4b-a8b0-9f2c2efef805";

    private static final UUID TRANSACTION_ID = UUID.fromString("221dc9a8-81e7-4bee-afc8-3cd83aae580d");

    @Autowired
    private KafkaTemplate<String, TransactionPayload> kafkaTemplate;

    private MailRequest createMailRequest() {
        var request = random(MailRequest.class);
        request.setUserId(USER_ID);
        return request;
    }

    private TransactionPayload createTransactionPayload() {
        var event = random(TransactionPayload.class);
        event.setTransactionId(TRANSACTION_ID.toString());
        event.setUserId(USER_ID);
        return event;
    }

    @Test
    @DisplayName("Should save mail successfully")
    void shouldProcessMailSuccessfully() {
        var mailRequest = createMailRequest();
        var expectedResponse = new MailResponse("Mail saved successfully");

        var actualResponse = doPost("/api/mails", mailRequest, MailResponse.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should consume event and send mail")
    void shouldConsumeEventAndSendMail(CapturedOutput output) {
        var event = createTransactionPayload();
        kafkaTemplate.send("transaction-topic", event.getTransactionId(), event);
        var mailRequest = createMailRequest();
        doPost("/api/mails", mailRequest, MailResponse.class);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(output).contains("was send to email:");
        });
    }
}