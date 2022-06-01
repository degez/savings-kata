package com.yucel.savings.exception;

public class BalanceCannotBeNegativeException extends RuntimeException {

    public BalanceCannotBeNegativeException(String message) {
        super(message);
    }
}
