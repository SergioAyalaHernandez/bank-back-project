package com.sergio.bank.model;

import com.sergio.bank.exception.InsufficientFundsException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SAVINGS")
@Getter
@Setter
public class SavingsAccount extends Account {
    private BigDecimal interestRate;

    @Override
    public void debit(BigDecimal amount) {
        if (getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        setBalance(getBalance().subtract(amount));
    }

    @Override
    public void credit(BigDecimal amount) {
        setBalance(getBalance().add(amount));
    }
}