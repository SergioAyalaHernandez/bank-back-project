package com.sergio.bank.service;

import com.sergio.bank.dto.AccountDTO;
import com.sergio.bank.dto.CustomerDTO;
import com.sergio.bank.exception.BadRequestException;
import com.sergio.bank.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO) throws BadRequestException;
    CustomerDTO getCustomer(Long id);
    List<AccountDTO> getCustomerAccounts(Long id);
    void deleteCustomer(Long id) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException, BadRequestException;
}

