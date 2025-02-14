package com.pdp.transaction.service;

import com.pdp.transaction.api.request.MailRequest;
import com.pdp.transaction.api.response.MailResponse;
import reactor.core.publisher.Mono;

public interface MailService {

    Mono<MailResponse> mails(MailRequest request);
}
