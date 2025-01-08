package com.sergio.bank.observer;

import com.sergio.bank.event.TransactionEvent;
import com.sergio.bank.model.TransactionLog;
import com.sergio.bank.repository.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionLogger implements TransactionObserver {

    private final TransactionLogRepository transactionLogRepository;

    @Autowired
    public TransactionLogger(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @Override
    public void onTransactionCompleted(TransactionEvent event) {
        TransactionLog log = new TransactionLog();
        log.setTransactionType(event.getType());
        log.setTimestamp(event.getTimestamp());
        log.setAmount(event.getAmount());
        log.setSourceAccountId(event.getSourceAccountId());
        log.setDestinationAccountId(event.getDestinationAccountId());
        log.setStatus(event.getStatus());

        transactionLogRepository.save(log);
    }
}
