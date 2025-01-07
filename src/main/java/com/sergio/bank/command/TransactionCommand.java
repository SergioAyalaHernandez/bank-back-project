package com.sergio.bank.command;

public interface TransactionCommand {
    void execute();
    void undo();
}
