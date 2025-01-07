package com.sergio.bank.factory;

import com.sergio.bank.model.Account;
import com.sergio.bank.model.CheckingAccount;
import com.sergio.bank.model.Customer;
import com.sergio.bank.model.SavingsAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountFactory {
    public Account createAccount(String type, Customer customer) {
        Account account = switch (type) {
            case "SAVINGS" -> new SavingsAccount();
            case "CHECKING" -> new CheckingAccount();
            default -> throw new IllegalArgumentException("Invalid account type: " + type);
        };
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ZERO);
        return account;
    }


}
