package com.sergio.bank.service;

import com.sergio.bank.command.TransactionCommand;
import com.sergio.bank.command.TransferCommand;
import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.exception.AccountNotFoundException;
import com.sergio.bank.factory.AccountFactory;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.Customer;
import com.sergio.bank.observer.TransactionObserver;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.strategy.TransactionStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountFactory accountFactory;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private List<TransactionObserver> transactionObservers;

    public AccountService(AccountRepository accountRepository, AccountFactory accountFactory, CustomerMapper customerMapper, AccountMapper accountMapper, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
        this.customerMapper = customerMapper;
        this.accountMapper = accountMapper;
        this.customerService = customerService;
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Customer customer = customerMapper.toEntity(customerService.getCustomer(accountDTO.getCustomerId()));
        Account account = accountFactory.createAccount(accountDTO.getType(), customer);
        account = accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDTO)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public void transfer(Long sourceId, Long destinationId, BigDecimal amount) {
        Account sourceAccount = accountRepository.findById(sourceId)
                .orElseThrow(() -> new AccountNotFoundException(sourceId));
        Account destinationAccount = accountRepository.findById(destinationId)
                .orElseThrow(() -> new AccountNotFoundException(destinationId));

        TransactionCommand transferCommand = new TransferCommand(
                sourceAccount,
                destinationAccount,
                amount,
                transactionObservers
        );

        transferCommand.execute();

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }
}
