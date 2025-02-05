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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(PasswordEncoder passwordEncoder, AccountMapper accountMapper, AccountRepository accountRepository, CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws BadRequestException {
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new BadRequestException(MessageConstants.ERROR_EXIST_EMAIL);
        }
        Customer customer = customerMapper.toEntity(customerDTO);
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(savedCustomer);
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
                .toList();
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        List<Account> accounts = accountRepository.findByCustomerId(id);
        accountRepository.deleteAll(accounts);
        customerRepository.delete(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException, BadRequestException {
        Customer existingCustomer = findCustomerById(id);
        validateEmail(customerDTO.getEmail(), existingCustomer);
        customerMapper.updateEntityFromDto(customerDTO, existingCustomer);
        if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
            existingCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        }
        return saveAndReturnCustomer(existingCustomer);
    }

    private Customer findCustomerById(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private void validateEmail(String email, Customer existingCustomer) throws BadRequestException {
        if (!existingCustomer.getEmail().equals(email) && customerRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException(MessageConstants.ERROR_EXIST_EMAIL);
        }
    }

    private CustomerDTO saveAndReturnCustomer(Customer existingCustomer) {
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDTO(updatedCustomer);
    }
}
