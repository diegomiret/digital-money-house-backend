package com.digitalMoneyHouse.accountsservice.exceptions;

public class TransactionGoneException extends RuntimeException {
    public TransactionGoneException(String message) {
        super(message);
    }
}