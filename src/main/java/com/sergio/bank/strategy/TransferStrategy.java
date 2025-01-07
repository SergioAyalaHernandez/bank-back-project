package com.sergio.bank.strategy;

import com.sergio.bank.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferStrategy implements TransactionStrategy {
    @Override
    public void execute(Account source, Account destination, BigDecimal amount) {
        source.debit(amount);
        destination.credit(amount);
    }
}