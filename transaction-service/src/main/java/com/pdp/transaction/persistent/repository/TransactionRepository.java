package com.pdp.transaction.persistent.repository;

import com.pdp.transaction.persistent.postgres.entity.TransactionsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TransactionRepository extends R2dbcRepository<TransactionsEntity, Long> {

}

