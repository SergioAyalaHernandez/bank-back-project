package com.sergio.bank.service;


public interface MessagePublisherService {
    void publishAccountMessage(String transactionType, String accountId, String userId, boolean status);
}
