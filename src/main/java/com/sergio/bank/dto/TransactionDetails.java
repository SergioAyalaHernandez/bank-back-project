package com.sergio.bank.dto;

import java.math.BigDecimal;

public class TransactionDetails {
    private String transactionType;
    private BigDecimal amount;

    public TransactionDetails(String transactionType, BigDecimal amount) {
        this.transactionType = transactionType;
        this.amount = amount;
    }


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
