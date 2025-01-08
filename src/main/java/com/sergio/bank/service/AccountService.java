package com.sergio.bank.service;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.model.Account;
import com.sergio.bank.observer.TransactionObserver;

import java.math.BigDecimal;

public interface AccountService {
    void registerObserver(TransactionObserver observer);

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccount(Long id);

    void performTransaction(String transactionType, Account source, Account destination, BigDecimal amount);
}
