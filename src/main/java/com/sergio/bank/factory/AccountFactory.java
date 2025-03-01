package com.sergio.bank.factory;

import com.sergio.bank.model.Account;
import com.sergio.bank.model.CheckingAccount;
import com.sergio.bank.model.SavingsAccount;
import com.sergio.bank.util.MessageConstants;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountFactory {
    public Account createAccount(String type, Long customerId, BigDecimal balance, Long accountNumber) {
        Account account = switch (type) {
            case MessageConstants.ACCOUNT_TYPE_SAVINGS -> new SavingsAccount();
            case MessageConstants.ACCOUNT_TYPE_CHECKING -> new CheckingAccount();
            default -> throw new IllegalArgumentException(MessageConstants.ERROR_INVALID_ACCOUNT_TYPE + type);
        };
        account.setCustomerId(customerId);
        account.setBalance(balance);
        account.setAccountNumber(accountNumber);
        return account;
    }
}
