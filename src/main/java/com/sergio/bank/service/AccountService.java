package com.sergio.bank.service;

import com.sergio.bank.dto.AccountDTO;

import java.math.BigDecimal;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccount(Long id);

    AccountDTO updateBalance(Long id, BigDecimal newBalance);

    AccountDTO getAccountsByCustomerId(Long id);

}
