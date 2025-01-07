package com.sergio.bank.strategy;

import com.sergio.bank.model.Account;

import java.math.BigDecimal;

public interface TransactionStrategy {
    void execute(Account source, Account destination, BigDecimal amount);
}
