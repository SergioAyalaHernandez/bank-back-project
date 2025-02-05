package com.sergio.bank.service.impl;

import com.sergio.bank.model.TransactionLog;
import com.sergio.bank.repository.TransactionLogRepository;
import com.sergio.bank.service.TransactionLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogServiceImpl implements TransactionLogService {

    private final TransactionLogRepository transactionLogRepository;

    public TransactionLogServiceImpl(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @Override
    public Page<TransactionLog> getAllTransactionLogs(Pageable pageable) {
        return transactionLogRepository.findAll(pageable);
    }
}
