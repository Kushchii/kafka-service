package com.pdp.transaction.mapper;

import com.pdp.transaction.api.request.TransactionsRequest;
import com.pdp.transaction.persistent.postgres.entity.TransactionsEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionsEntity toEntity(TransactionsRequest request) {
        var transaction = new TransactionsEntity();
        transaction.setTransactionId(request.getId());
        transaction.setAmount(request.getAmount());
        transaction.setUserId(request.getUserId());
        transaction.setCurrency(request.getCurrency());
        transaction.setStatus(request.getStatus());
        transaction.setDescription(request.getDescription());

        return transaction;
    }
}
