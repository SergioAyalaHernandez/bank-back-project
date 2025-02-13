package com.sergio.bank.service;

import com.sergio.bank.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccount(Long id);

    AccountDTO updateBalance(Long id, BigDecimal newBalance);

    List<AccountDTO> getAccountsByCustomerId(Long id);

}
