package com.sergio.bank.service.impl;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.TransactionDetails;
import com.sergio.bank.event.TransactionEvent;
import com.sergio.bank.exception.AccountNotFoundException;
import com.sergio.bank.factory.AccountFactory;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.Customer;
import com.sergio.bank.observer.TransactionLogger;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.service.AccountService;
import com.sergio.bank.strategy.TransactionContext;
import com.sergio.bank.strategy.impl.DepositStrategy;
import com.sergio.bank.strategy.impl.TransferStrategy;
import com.sergio.bank.strategy.impl.WithdrawalStrategy;
import com.sergio.bank.util.MessageConstants;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final AccountMapper accountMapper;
    private final CustomerMapper customerMapper;
    private final CustomerServiceImpl customerService;
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
        Long accountNumber = accountDTO.getCustomerId() + LocalDateTime.now().getNano();
        Account account = accountFactory.createAccount(accountDTO.getType(), customer, accountDTO.getBalance(),accountNumber);
        account = accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDTO)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public TransactionDetails performTransaction(String transactionType, Long sourceId, Long destinationId, BigDecimal amount) {
        Account source = findAccountById(sourceId);
        Account destination = findAccountById(destinationId);

        setTransactionStrategy(transactionType);

        transactionContext.executeTransaction(source, destination, amount);

        saveAccounts(source, destination);

        TransactionEvent event = createTransactionEvent(transactionType, amount, source, destination);
        transactionContext.notifyObservers(event);

        return new TransactionDetails(transactionType.toUpperCase(),amount);
    }

    private Account findAccountById(Long accountId) {
        if (accountId != null) {
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format(MessageConstants.ERROR_ACCOUNT_NOT_FOUND, accountId)));
        }
        return null;
    }

    private void setTransactionStrategy(String transactionType) {
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
    }

    private void saveAccounts(Account source, Account destination) {
        if (source != null) {
            accountRepository.save(source);
        }
        if (destination != null) {
            accountRepository.save(destination);
        }
    }

    private TransactionEvent createTransactionEvent(String transactionType, BigDecimal amount, Account source, Account destination) {
        return new TransactionEvent(
                transactionType.toUpperCase(),
                LocalDateTime.now(),
                amount,
                source != null ? source.getId() : null,
                destination != null ? destination.getId() : null,
                MessageConstants.TRANSACTION_STATUS_SUCCESS
        );
    }

    @Override
    public AccountDTO updateBalance(Long id, BigDecimal newBalance) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.setBalance(newBalance);
        account = accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

}
