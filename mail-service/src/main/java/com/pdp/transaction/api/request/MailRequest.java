package com.pdp.transaction.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MailRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String email;
}
