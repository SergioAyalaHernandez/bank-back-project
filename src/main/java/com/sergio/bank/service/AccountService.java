package com.sergio.bank.service;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.model.Account;
import com.sergio.bank.observer.TransactionObserver;

import java.math.BigDecimal;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccount(Long id);

    void performTransaction(String transactionType, Long sourceId, Long destinationId, BigDecimal amount);
}
