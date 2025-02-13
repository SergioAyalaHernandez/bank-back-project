package com.sergio.bank.controller;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.UpdateBalanceRequest;
import com.sergio.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{id}/balance")
    public ResponseEntity<AccountDTO> updateBalance(@PathVariable Long id, @RequestBody UpdateBalanceRequest request) {
        return ResponseEntity.ok(accountService.updateBalance(id, request.getNewBalance()));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<AccountDTO>> getAccountsByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(id));
    }
}
