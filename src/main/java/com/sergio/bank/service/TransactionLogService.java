package com.sergio.bank.service;

import com.sergio.bank.model.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionLogService {
    Page<TransactionLog> getAllTransactionLogs(Pageable pageable);
}
