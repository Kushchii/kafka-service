package com.pdp.transaction.service;

import com.pdp.transaction.api.request.MailRequest;
import com.pdp.transaction.api.response.MailResponse;
import com.pdp.transaction.mapper.MailMapper;
import com.pdp.transaction.persistent.repository.MailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final MailMapper mailMapper;

    private final MailsRepository mailsRepository;

    @Override
    public Mono<MailResponse> mails(MailRequest request) {
        var entity = mailMapper.toEntity(request);
        return mailsRepository.save(entity)
                .doOnNext(mail -> log.info("Mail with id: {} was saved", mail.getUserId()))
                .flatMap(mail -> Mono.just(new MailResponse("Mail saved successfully")));
    }
}
