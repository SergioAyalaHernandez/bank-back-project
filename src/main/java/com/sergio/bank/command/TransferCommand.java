package com.sergio.bank.command;

import com.sergio.bank.event.TransactionEvent;
import com.sergio.bank.model.Account;
import com.sergio.bank.observer.TransactionObserver;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TransferCommand implements TransactionCommand {
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final BigDecimal amount;
    private final List<TransactionObserver> observers;

    public TransferCommand(Account sourceAccount, Account destinationAccount,
                           BigDecimal amount, List<TransactionObserver> observers) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.observers = observers;
    }

    @Override
    public void execute() {
        try {
            sourceAccount.debit(amount);
            destinationAccount.credit(amount);

            TransactionEvent event = new TransactionEvent();
            event.setType("TRANSFER");
            event.setTimestamp(LocalDateTime.now());
            event.setAmount(amount);
            event.setSourceAccountId(sourceAccount.getId());
            event.setDestinationAccountId(destinationAccount.getId());
            event.setStatus("SUCCESS");

            observers.forEach(observer -> observer.onTransactionCompleted(event));

        } catch (Exception e) {
            TransactionEvent event = new TransactionEvent();
            event.setType("TRANSFER");
            event.setTimestamp(LocalDateTime.now());
            event.setAmount(amount);
            event.setSourceAccountId(sourceAccount.getId());
            event.setDestinationAccountId(destinationAccount.getId());
            event.setStatus("FAILED");

            observers.forEach(observer -> observer.onTransactionCompleted(event));

            throw e;
        }
    }

    @Override
    public void undo() {
        sourceAccount.credit(amount);
        destinationAccount.debit(amount);
    }
}
