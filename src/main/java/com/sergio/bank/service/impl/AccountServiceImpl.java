package com.sergio.bank.service.impl;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.event.TransactionEvent;
import com.sergio.bank.exception.AccountNotFoundException;
import com.sergio.bank.factory.AccountFactory;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.Customer;
import com.sergio.bank.observer.TransactionLogger;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.strategy.TransactionContext;
import com.sergio.bank.strategy.impl.DepositStrategy;
import com.sergio.bank.strategy.impl.TransferStrategy;
import com.sergio.bank.strategy.impl.WithdrawalStrategy;
import com.sergio.bank.util.MessageConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class AccountServiceImpl {
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

    public AccountServiceImpl(AccountRepository accountRepository, AccountFactory accountFactory, CustomerMapper customerMapper, AccountMapper accountMapper, CustomerServiceImpl customerService, TransactionContext transactionContext, TransactionLogger transactionLogger) {
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
        this.customerMapper = customerMapper;
        this.accountMapper = accountMapper;
        this.customerService = customerService;
        this.transactionContext = transactionContext;
        this.transactionContext.registerObserver(transactionLogger);
    }



    public AccountDTO createAccount(AccountDTO accountDTO) {
        Customer customer = customerMapper.toEntity(customerService.getCustomer(accountDTO.getCustomerId()));
        Account account = accountFactory.createAccount(accountDTO.getType(), customer, accountDTO.getBalance());
        account = accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDTO)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public void performTransaction(String transactionType, Long sourceId, Long destinationId, BigDecimal amount) {
        Account source = sourceId != null ? accountRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(MessageConstants.ERROR_ACCOUNT_NOT_FOUND, sourceId))) : null;

        Account destination = destinationId != null ? accountRepository.findById(destinationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format(MessageConstants.ERROR_ACCOUNT_NOT_FOUND, destinationId))) : null;


        switch (transactionType.toUpperCase()) {
            case MessageConstants.TRANSACTION_TYPE_TRANSFER:
                transactionContext.setTransactionStrategy(new TransferStrategy());
                break;
            case MessageConstants.TRANSACTION_TYPE_DEPOSIT:
                transactionContext.setTransactionStrategy(new DepositStrategy());
                break;
            case MessageConstants.TRANSACTION_TYPE_WITHDRAWAL:
                transactionContext.setTransactionStrategy(new WithdrawalStrategy());
                break;
            default:
                throw new IllegalArgumentException(MessageConstants.ERROR_UNSUPPORTED_TRANSACTION_TYPE);
        }

        transactionContext.executeTransaction(source, destination, amount);

        if (source != null) {
            accountRepository.save(source);
        }
        if (destination != null) {
            accountRepository.save(destination);
        }

        TransactionEvent event = new TransactionEvent(
                transactionType.toUpperCase(),
                LocalDateTime.now(),
                amount,
                source != null ? source.getId() : null,
                destination != null ? destination.getId() : null,
                MessageConstants.TRANSACTION_STATUS_SUCCESS
        );
        transactionContext.notifyObservers(event);
    }

}
