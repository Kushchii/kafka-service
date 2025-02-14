package com.pdp.transaction;

import com.pdp.transaction.api.request.MailRequest;
import com.pdp.transaction.api.response.MailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionFunctionalTest extends BaseFunctionalTest {

    private static final String USER_ID = "1e0d2473-1396-4d4b-a8b0-9f2c2efef805";
    private static final String EMAIL = "anton@gmail.com";


    private MailRequest createMailRequest() {
        var request = new MailRequest();
        request.setUserId(USER_ID);
        request.setEmail(EMAIL);
        return request;
    }

    @Test
    @DisplayName("Successful mail")
    void shouldProcessMailSuccessfully() {
        var mailRequest = createMailRequest();
        var expectedResponse = new MailResponse("Mail saved successfully");

        var actualResponse = doPost("/api/mails", mailRequest, MailResponse.class);

        assertEquals(expectedResponse, actualResponse);
    }

}
