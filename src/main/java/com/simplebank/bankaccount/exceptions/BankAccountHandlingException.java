package com.simplebank.bankaccount.exceptions;

public class BankAccountHandlingException extends IllegalArgumentException {
    public BankAccountHandlingException(String errMsg) {
        super(errMsg);
    }
}
