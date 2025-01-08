package com.sergio.bank.exception;

public class HttpRequestMethodNotSupportedException extends RuntimeException{
    public HttpRequestMethodNotSupportedException() {
        super("Insufficient funds for this transaction");
    }

}
