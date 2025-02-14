package com.pdp.transaction.service;

import com.pdp.transaction.api.request.TransactionsRequest;
import com.pdp.transaction.api.response.TransactionsResponse;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<TransactionsResponse> transactions(TransactionsRequest request);
}
