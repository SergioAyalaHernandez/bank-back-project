package com.sergio.bank.controller;

import com.sergio.bank.model.TransactionLog;
import com.sergio.bank.service.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionLogController {

    @Autowired
    private TransactionLogService transactionLogService;

    @GetMapping("/transaction-logs")
    public Page<TransactionLog> getTransactionLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return transactionLogService.getAllTransactionLogs(pageable);
    }
}
