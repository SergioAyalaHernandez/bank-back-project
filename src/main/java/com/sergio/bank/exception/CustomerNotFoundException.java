package com.sergio.bank.exception;

import com.sergio.bank.util.MessageConstants;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super(MessageConstants.ERROR_CUSTOMER_NOT_FOUND + id);
    }
}