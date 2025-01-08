package com.sergio.bank.strategy;

import com.sergio.bank.event.TransactionEvent;
import com.sergio.bank.model.Account;
import com.sergio.bank.observer.TransactionObserver;
import com.sergio.bank.util.MessageConstants;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionContext {

    private TransactionStrategy transactionStrategy;
    private final List<TransactionObserver> observers = new ArrayList<>();

    public void setTransactionStrategy(TransactionStrategy transactionStrategy) {
        this.transactionStrategy = transactionStrategy;
    }

    public void executeTransaction(Account source, Account destination, BigDecimal amount) {
        if (transactionStrategy == null) {
            throw new IllegalStateException(MessageConstants.ERROR_STRATEGY);
        }
        transactionStrategy.execute(source, destination, amount);
    }

    public void registerObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(TransactionEvent event) {
        for (TransactionObserver observer : observers) {
            observer.onTransactionCompleted(event);
        }
    }
}
