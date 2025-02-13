package com.sergio.bank.service.impl;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.exception.AccountNotFoundException;
import com.sergio.bank.exception.CustomerNotFoundException;
import com.sergio.bank.factory.AccountFactory;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.service.AccountService;
import com.sergio.bank.util.MessageConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final AccountMapper accountMapper;
    private final RestTemplate restTemplate;
    private final String userMicroServiceUrl;
    private final MessagePublisherServiceImpl messagePublisherService;

    public AccountServiceImpl(AccountRepository accountRepository,
                              AccountFactory accountFactory,
                              AccountMapper accountMapper,
                              RestTemplate restTemplate,
                              @Value("${users.microservice.url}") String userMicroServiceUrl,
                              MessagePublisherServiceImpl messagePublisherService) {
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
        this.accountMapper = accountMapper;
        this.restTemplate = restTemplate;
        this.userMicroServiceUrl = userMicroServiceUrl;
        this.messagePublisherService = messagePublisherService;
    }


    public AccountDTO createAccount(AccountDTO accountDTO) {
//        String url = userMicroServiceUrl + "/customers/" + accountDTO.getCustomerId();
//
//        try {
//            ResponseEntity<CustomerDTO> response = restTemplate.getForEntity(url, CustomerDTO.class);
//            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
//                throw new CustomerNotFoundException(accountDTO.getCustomerId());
//            }
//        } catch (HttpClientErrorException.NotFound e) {
//            throw new CustomerNotFoundException(accountDTO.getCustomerId());
//        }

        Long accountNumber = accountDTO.getCustomerId() + LocalDateTime.now().getNano();
        Account account = accountFactory.createAccount(accountDTO.getType(), accountDTO.getCustomerId(), accountDTO.getBalance(),accountNumber);
        account = accountRepository.save(account);

        messagePublisherService.publishAccountMessage("creación", String.valueOf(accountNumber), String.valueOf(account.getCustomerId()), true);
        return accountMapper.toDTO(account);
    }

    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDTO)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private Account findAccountById(Long accountId) {
        if (accountId != null) {
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format(MessageConstants.ERROR_ACCOUNT_NOT_FOUND, accountId)));
        }
        return null;
    }

    @Override
    public AccountDTO updateBalance(Long id, BigDecimal newBalance) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.setBalance(newBalance);
        account = accountRepository.save(account);
        messagePublisherService.publishAccountMessage("actualización", String.valueOf(account.getAccountNumber()), String.valueOf(account.getCustomerId()), true);
        return accountMapper.toDTO(account);
    }

    @Override
    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        String url = userMicroServiceUrl + "/customers/" + customerId;
        try {
            ResponseEntity<CustomerDTO> response = restTemplate.getForEntity(url, CustomerDTO.class);
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new CustomerNotFoundException(customerId);
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new CustomerNotFoundException(customerId);
        }

        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        if (accounts.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format(MessageConstants.ERROR_ACCOUNTS_NOT_FOUND_BY_CUSTOMER, customerId));
        }
        return accounts.stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

}
