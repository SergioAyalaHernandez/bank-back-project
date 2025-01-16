package com.sergio.bank.controller;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.TransactionDTO;
import com.sergio.bank.dto.TransactionDetails;
import com.sergio.bank.dto.UpdateBalanceRequest;
import com.sergio.bank.service.AccountService;
import com.sergio.bank.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        return ResponseEntity.ok(accountService.createAccount(accountDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PostMapping("/transaction")
    public ResponseEntity<TransactionDetails> performTransaction(@RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(accountService.performTransaction(
                transactionDTO.getTransactionType(), // Tipo de transacción (TRANSFER, DEPOSIT, WITHDRAWAL)
                transactionDTO.getSourceAccountId(),
                transactionDTO.getDestinationAccountId(),
                transactionDTO.getAmount()
        ));
    }

    @PostMapping("/{id}/balance")
    public ResponseEntity<AccountDTO> updateBalance(@PathVariable Long id, @RequestBody UpdateBalanceRequest request) {
        return ResponseEntity.ok(accountService.updateBalance(id, request.getNewBalance()));
    }

}
