package com.sergio.bank.strategy;

import com.sergio.bank.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionContext {

    private TransactionStrategy transactionStrategy;

    public void setTransactionStrategy(TransactionStrategy transactionStrategy) {
        this.transactionStrategy = transactionStrategy;
    }

    public void executeTransaction(Account source, Account destination, BigDecimal amount) {
        if (transactionStrategy == null) {
            throw new IllegalStateException("Transaction strategy not set");
        }
        transactionStrategy.execute(source, destination, amount);
    }
}
