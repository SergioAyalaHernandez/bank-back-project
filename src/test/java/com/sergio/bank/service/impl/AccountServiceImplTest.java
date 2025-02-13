package com.sergio.bank.service.impl;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.exception.AccountNotFoundException;
import com.sergio.bank.exception.CustomerNotFoundException;
import com.sergio.bank.factory.AccountFactory;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.SavingsAccount;
import com.sergio.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountFactory accountFactory;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MessagePublisherServiceImpl messagePublisherService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDTO accountDTO;
    private Account account;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setType("SAVINGS");
        accountDTO.setBalance(new BigDecimal("1000.00"));

        account = new SavingsAccount();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    void createAccount_ShouldCreateAccountSuccessfully() {
        when(accountFactory.createAccount(eq(accountDTO.getType()),
                eq(accountDTO.getCustomerId()),
                eq(accountDTO.getBalance()),
                anyLong()))
                .thenAnswer(invocation -> {
                    SavingsAccount newAccount = new SavingsAccount();
                    newAccount.setId(1L);
                    newAccount.setBalance(accountDTO.getBalance());
                    newAccount.setAccountNumber(invocation.getArgument(3));
                    return newAccount;
                });
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);

        AccountDTO result = accountService.createAccount(accountDTO);

        assertNotNull(result);
        assertEquals(accountDTO, result);
        verify(messagePublisherService).publishAccountMessage(eq("creación"), anyString(), anyString(), eq(true));
    }

    @Test
    void getAccount_WhenAccountExists_ShouldReturnAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toDTO(account)).thenReturn(accountDTO);

        AccountDTO result = accountService.getAccount(1L);

        assertNotNull(result);
        assertEquals(accountDTO, result);
    }

    @Test
    void getAccount_WhenAccountNotExists_ShouldThrowException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(1L));
    }

    @Test
    void updateBalance_ShouldUpdateBalanceSuccessfully() {
        Long accountId = 1L;
        BigDecimal newBalance = new BigDecimal("1500.00");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountDTO updatedDTO = new AccountDTO();
        updatedDTO.setCustomerId(accountDTO.getCustomerId());
        updatedDTO.setType(accountDTO.getType());
        updatedDTO.setBalance(newBalance);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(updatedDTO);

        AccountDTO result = accountService.updateBalance(accountId, newBalance);

        assertNotNull(result);
        assertEquals(newBalance, result.getBalance());
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(any(Account.class));
        verify(messagePublisherService).publishAccountMessage(eq("actualización"), anyString(), anyString(), eq(true));
    }

    @Test
    void getAccountsByCustomerId_WhenAccountsExist_ShouldReturnAccounts() {
        Long customerId = 1L;

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerId);
        ResponseEntity<CustomerDTO> responseEntity = new ResponseEntity<>(customerDTO, HttpStatus.OK);

        lenient().when(restTemplate.getForEntity(anyString(), eq(CustomerDTO.class)))
                .thenReturn(responseEntity);

        List<Account> accounts = List.of(account);
        when(accountRepository.findByCustomerId(customerId)).thenReturn(accounts);
        when(accountMapper.toDTO(account)).thenReturn(accountDTO);

        List<AccountDTO> result = accountService.getAccountsByCustomerId(customerId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(accountDTO, result.get(0));
    }

    @Test
    void getAccountsByCustomerId_WhenNoAccountsFound_ShouldThrowException() {
        Long customerId = 1L;

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerId);
        ResponseEntity<CustomerDTO> responseEntity = new ResponseEntity<>(customerDTO, HttpStatus.OK);

        lenient().when(restTemplate.getForEntity(anyString(), eq(CustomerDTO.class)))
                .thenReturn(responseEntity);

        when(accountRepository.findByCustomerId(customerId)).thenReturn(List.of());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.getAccountsByCustomerId(customerId));
        assertTrue(exception.getMessage().contains("No se encontraron cuentas"));
    }


}