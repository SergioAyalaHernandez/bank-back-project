package com.sergio.bank.service.impl;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.exception.AccountNotFoundException;
import com.sergio.bank.factory.AccountFactory;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.Customer;
import com.sergio.bank.model.SavingsAccount;
import com.sergio.bank.observer.TransactionLogger;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.strategy.TransactionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private CustomerMapper customerMapper;

    @Mock
    private CustomerServiceImpl customerService;

    @Mock
    private TransactionContext transactionContext;

    @Mock
    private TransactionLogger transactionLogger;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDTO accountDTO;
    private Account account;
    private Customer customer;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setType("SAVINGS");
        accountDTO.setBalance(new BigDecimal("1000.00"));

        customer = new Customer();
        customer.setId(1L);

        account = new SavingsAccount();
        account.setId(1L);
        account.setCustomer(customer);
        account.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    void createAccount_ShouldCreateAndReturnAccount() {
        when(customerService.getCustomer(1L)).thenReturn(new CustomerDTO());
        when(customerMapper.toEntity(any(CustomerDTO.class))).thenReturn(customer);
        when(accountFactory.createAccount(anyString(), any(Customer.class), any(BigDecimal.class)))
                .thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);

        AccountDTO result = accountService.createAccount(accountDTO);

        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
        verify(accountMapper).toDTO(account);
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
    void performTransaction_Transfer_ShouldExecuteSuccessfully() {
        Account sourceAccount = new SavingsAccount();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("1000.00"));

        Account destAccount = new SavingsAccount();
        destAccount.setId(2L);
        destAccount.setBalance(new BigDecimal("1000.00"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destAccount));

        accountService.performTransaction(
                "TRANSFER",
                1L,
                2L,
                new BigDecimal("100.00")
        );

        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionContext).executeTransaction(
                any(Account.class),
                any(Account.class),
                any(BigDecimal.class)
        );
    }
}