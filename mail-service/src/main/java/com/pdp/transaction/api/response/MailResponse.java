package com.pdp.transaction.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MailResponse {

    private String message;

    @JsonCreator
    public MailResponse(@JsonProperty("message") String message) {
        this.message = message;
    }
}
