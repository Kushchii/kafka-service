package com.pdp.transaction.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionsResponse {

    private String message;

    @JsonCreator
    public TransactionsResponse(@JsonProperty("message") String message) {
        this.message = message;
    }
}
