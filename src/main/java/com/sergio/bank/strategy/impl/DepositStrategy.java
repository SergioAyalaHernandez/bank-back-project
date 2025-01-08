package com.sergio.bank.strategy.impl;

import com.sergio.bank.model.Account;
import com.sergio.bank.strategy.TransactionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(Account source, Account destination, BigDecimal amount) {
        destination.credit(amount);
    }
}
