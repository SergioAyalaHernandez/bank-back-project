package com.sergio.bank.controller;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.TransactionDTO;
import com.sergio.bank.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private AccountServiceImpl accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountDTO accountDTO;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setType("SAVINGS");
        accountDTO.setBalance(new BigDecimal("1000.00"));

        transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionType("TRANSFER");
        transactionDTO.setSourceAccountId(1L);
        transactionDTO.setDestinationAccountId(2L);
        transactionDTO.setAmount(new BigDecimal("100.00"));
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.createAccount(accountDTO);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(accountDTO, response.getBody())
        );
        verify(accountService).createAccount(accountDTO);
    }

    @Test
    void getAccount_ShouldReturnAccount() {
        when(accountService.getAccount(1L)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.getAccount(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(accountDTO, response.getBody())
        );
        verify(accountService).getAccount(1L);
    }

    @Test
    void performTransaction_ShouldExecuteSuccessfully() {
        doNothing().when(accountService).performTransaction(
                anyString(), anyLong(), anyLong(), any(BigDecimal.class));

        ResponseEntity<Void> response = accountController.performTransaction(transactionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountService).performTransaction(
                transactionDTO.getTransactionType(),
                transactionDTO.getSourceAccountId(),
                transactionDTO.getDestinationAccountId(),
                transactionDTO.getAmount()
        );
    }
}