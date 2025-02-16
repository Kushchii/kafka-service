package com.pdp.transaction;

import com.pdp.transaction.api.request.TransactionsRequest;
import com.pdp.transaction.api.response.TransactionsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionFunctionalTest extends BaseFunctionalTest {

    private static final UUID TRANSACTION_ID = UUID.fromString("221dc9a8-81e7-4bee-afc8-3cd83aae580d");
    private static final String USER_ID = "1e0d2473-1396-4d4b-a8b0-9f2c2efef805";
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_SUCCESS = "success";

    private TransactionsRequest createTransactionRequest(String status) {
        var request = random(TransactionsRequest.class);
        request.setId(TRANSACTION_ID);
        request.setStatus(status);
        request.setUserId(USER_ID);
        return request;
    }

    @Test
    @DisplayName("Successful transaction")
    void shouldProcessTransactionSuccessfully() {
        var transactionsRequest = createTransactionRequest(STATUS_PENDING);
        var expectedResponse = new TransactionsResponse("Transaction processed successfully");

        var actualResponse = doPost("/api/transactions", transactionsRequest, TransactionsResponse.class);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Successful transaction and send message")
    void shouldProcessTransactionSuccessfullyAndSendMessage() {
        var transactionsRequest = createTransactionRequest(STATUS_SUCCESS);
        var expectedResponse = new TransactionsResponse("Transaction processed successfully");

        doPost("/api/transactions", transactionsRequest, TransactionsResponse.class);
        var actualResponse = doPost("/api/transactions", transactionsRequest, TransactionsResponse.class);


        assertEquals(expectedResponse, actualResponse);
    }
}
