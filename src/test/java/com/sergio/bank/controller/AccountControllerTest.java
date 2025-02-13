package com.sergio.bank.controller;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.UpdateBalanceRequest;
import com.sergio.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setType("SAVINGS");
        accountDTO.setBalance(new BigDecimal("1000.00"));
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
    void updateBalance_ShouldReturnUpdatedAccount() {
        Long accountId = 1L;
        UpdateBalanceRequest request = new UpdateBalanceRequest();
        request.setNewBalance(new BigDecimal("1500.00"));

        AccountDTO updatedAccountDTO = new AccountDTO();
        updatedAccountDTO.setCustomerId(1L);
        updatedAccountDTO.setType("SAVINGS");
        updatedAccountDTO.setBalance(new BigDecimal("1500.00"));

        when(accountService.updateBalance(accountId, request.getNewBalance())).thenReturn(updatedAccountDTO);

        ResponseEntity<AccountDTO> response = accountController.updateBalance(accountId, request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(updatedAccountDTO, response.getBody())
        );
        verify(accountService).updateBalance(accountId, request.getNewBalance());
    }

    @Test
    void getAccountsByCustomerId_ShouldReturnAccount() {
        Long customerId = 1L;
        when(accountService.getAccountsByCustomerId(customerId)).thenReturn(List.of(accountDTO));

        ResponseEntity<List<AccountDTO>> response = accountController.getAccountsByCustomerId(customerId);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertFalse(Objects.requireNonNull(response.getBody()).isEmpty()),
                () -> assertEquals(accountDTO, Objects.requireNonNull(response.getBody()).get(0))
        );
        verify(accountService).getAccountsByCustomerId(customerId);
    }

}