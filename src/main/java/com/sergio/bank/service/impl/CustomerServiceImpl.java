package com.sergio.bank.service.impl;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.exception.BadRequestException;
import com.sergio.bank.exception.CustomerNotFoundException;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Account;
import com.sergio.bank.model.Customer;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.repository.CustomerRepository;
import com.sergio.bank.service.CustomerService;
import com.sergio.bank.util.MessageConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws BadRequestException {
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new BadRequestException(MessageConstants.ERROR_EXIST_EMAIL);
        }
        Customer customer = mapToCustomer(customerDTO);
        customer.setPassword(encodePassword(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        return mapToDTO(savedCustomer);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private Customer mapToCustomer(CustomerDTO customerDTO) {
        return customerMapper.toEntity(customerDTO);
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return customerMapper.toDTO(customer);
    }

    public CustomerDTO getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public List<AccountDTO> getCustomerAccounts(Long id) {
        return accountRepository.findByCustomerId(id)
                .stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = findCustomerById(id);
        deleteAllAccountsForCustomer(id);
        deleteCustomerEntity(customer);
    }

    private void deleteAllAccountsForCustomer(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        accountRepository.deleteAll(accounts);
    }

    private void deleteCustomerEntity(Customer customer) {
        customerRepository.delete(customer);
    }


    private Customer copyAndUpdateCustomer(CustomerDTO customerDTO, Customer existingCustomer) {
        Customer updatedCustomer = new Customer(existingCustomer);
        customerMapper.updateEntityFromDto(customerDTO, updatedCustomer);
        if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
            updatedCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        }
        return updatedCustomer;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException, BadRequestException {
        Customer existingCustomer = findCustomerById(id);
        existingCustomer.setEmail(validateEmailOptional(customerDTO.getEmail(), existingCustomer));
        Customer updatedCustomer = copyAndUpdateCustomer(customerDTO, existingCustomer);
        return saveAndReturnCustomer(updatedCustomer);
    }

    private Customer findCustomerById(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private String validateEmailOptional(String email, Customer existingCustomer) {
        return Optional.ofNullable(email)
                .filter(e -> !existingCustomer.getEmail().equals(e))
                .filter(e -> customerRepository.findByEmail(e).isEmpty())
                .orElseThrow(() -> new BadRequestException(MessageConstants.ERROR_EXIST_EMAIL));
    }

    private CustomerDTO saveAndReturnCustomer(Customer existingCustomer) {
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDTO(updatedCustomer);
    }
}