package com.sergio.bank.dto;

import java.math.BigDecimal;

public class UpdateBalanceRequest {
    private BigDecimal newBalance;

    public BigDecimal getNewBalance() {
        return newBalance;
    }
    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }
}
