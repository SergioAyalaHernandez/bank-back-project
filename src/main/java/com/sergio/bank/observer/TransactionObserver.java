package com.sergio.bank.observer;

import com.sergio.bank.event.TransactionEvent;

public interface TransactionObserver {
    void onTransactionCompleted(TransactionEvent event);
}
