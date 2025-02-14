package com.pdp.transaction.handler;

import com.pdp.transaction.api.request.MailRequest;
import com.pdp.transaction.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@Validated
@RequiredArgsConstructor
public class MailHandler extends BaseHandler {

    private final MailService mails;

    public Mono<ServerResponse> mails(ServerRequest request) {
        return request.bodyToMono(MailRequest.class)
                .flatMap(mails::mails)
                .flatMap(it -> toServerResponse(HttpStatus.OK, it));
    }
}
