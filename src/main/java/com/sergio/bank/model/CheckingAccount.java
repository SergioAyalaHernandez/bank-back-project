package com.sergio.bank.model;

import com.sergio.bank.exception.InsufficientFundsException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CHECKING")
@Getter
@Setter
public class CheckingAccount extends Account {
    private BigDecimal overdraftLimit;

    @Override
    public void debit(BigDecimal amount) {
        BigDecimal minimumBalance = getBalance().subtract(overdraftLimit);
        if (minimumBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        setBalance(getBalance().subtract(amount));
    }

    @Override
    public void credit(BigDecimal amount) {
        setBalance(getBalance().add(amount));
    }
}
