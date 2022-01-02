package com.simplebank.customer.exceptions;

public class CustomerHandlingException extends IllegalArgumentException {
    public CustomerHandlingException(String errMsg) {
        super(errMsg);
    }
}