package com.sergio.bank.observer;

import com.sergio.bank.event.TransactionEvent;
import com.sergio.bank.model.TransactionLog;
import org.springframework.stereotype.Component;

@Component
public class TransactionLogger implements TransactionObserver {
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
