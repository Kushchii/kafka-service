package com.pdp.transaction.service;

import com.pdp.transaction.api.request.TransactionsRequest;
import com.pdp.transaction.api.response.TransactionsResponse;
import com.pdp.transaction.mapper.TransactionMapper;
import com.pdp.transaction.persistent.repository.TransactionRepository;
import com.pdp.transaction.service.kafka.producer.TransactionMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String TRANSACTION_SUCCESS = "SUCCESS";
    private final TransactionMapper transactionMapper;
    private final TransactionMessageProducer transactionMessageProducer;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<TransactionsResponse> transactions(TransactionsRequest request) {
        var entity = transactionMapper.toEntity(request);
        log.info("Transaction Entity created: {}", entity);

        return transactionRepository.save(entity)
                .doOnSuccess(savedEntity -> {
                    if (TRANSACTION_SUCCESS.equalsIgnoreCase(savedEntity.getStatus())) {
                        transactionMessageProducer.sendTransaction(savedEntity);
                    }
                })
                .thenReturn(new TransactionsResponse("Transaction processed successfully"));
    }
}
