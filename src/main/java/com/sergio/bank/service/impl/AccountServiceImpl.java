package com.sergio.bank.service;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.event.TransactionEvent;
import com.sergio.bank.exception.AccountNotFoundException;
import com.sergio.bank.factory.AccountFactory;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.Customer;
import com.sergio.bank.observer.TransactionObserver;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.service.impl.CustomerServiceImpl;
import com.sergio.bank.strategy.TransactionContext;
import com.sergio.bank.strategy.impl.DepositStrategy;
import com.sergio.bank.strategy.impl.TransferStrategy;
import com.sergio.bank.strategy.impl.WithdrawalStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private CustomerServiceImpl customerService;

    @Autowired
    private final TransactionContext transactionContext;

    private final List<TransactionObserver> observers = new ArrayList<>();

    public AccountService(AccountRepository accountRepository, AccountFactory accountFactory, CustomerMapper customerMapper, AccountMapper accountMapper, CustomerServiceImpl customerService, TransactionContext transactionContext) {
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
        this.customerMapper = customerMapper;
        this.accountMapper = accountMapper;
        this.customerService = customerService;
        this.transactionContext = transactionContext;
    }

    public void registerObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(TransactionEvent event) {
        for (TransactionObserver observer : observers) {
            observer.onTransactionCompleted(event);
        }
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

    @Transactional
    public void performTransaction(String transactionType, Account source, Account destination, BigDecimal amount) {
        switch (transactionType.toUpperCase()) {
            case "TRANSFER":
                transactionContext.setTransactionStrategy(new TransferStrategy());
                break;
            case "DEPOSIT":
                transactionContext.setTransactionStrategy(new DepositStrategy());
                break;
            case "WITHDRAWAL":
                transactionContext.setTransactionStrategy(new WithdrawalStrategy());
                break;
            default:
                throw new IllegalArgumentException("Unsupported transaction type");
        }

        transactionContext.executeTransaction(source, destination, amount);

        if (source != null) {
            accountRepository.save(source);
        }
        if (destination != null) {
            accountRepository.save(destination);
        }

        TransactionEvent event = new TransactionEvent(
                "TRANSFER",
                LocalDateTime.now(),
                amount,
                source != null ? source.getId() : null,
                destination != null ? destination.getId() : null,
                "SUCCESS"
        );
        notifyObservers(event);
    }
}
