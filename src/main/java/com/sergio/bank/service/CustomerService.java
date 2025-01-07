package com.sergio.bank.service;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.exception.CustomerNotFoundException;
import com.sergio.bank.mapper.AccountMapper;
import com.sergio.bank.mapper.CustomerMapper;
import com.sergio.bank.model.Customer;
import com.sergio.bank.repository.AccountRepository;
import com.sergio.bank.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setDocumentNumber(customerDTO.getDocumentNumber());
        customer = customerRepository.save(customer);
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
}