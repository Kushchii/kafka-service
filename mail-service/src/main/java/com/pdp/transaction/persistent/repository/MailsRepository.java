package com.pdp.transaction.persistent.repository;

import com.pdp.transaction.persistent.postgres.entity.MailsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface MailsRepository extends R2dbcRepository<MailsEntity, Long> {

    Mono<MailsEntity> findByUserId(String userId);
}

