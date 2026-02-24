package org.example.ordermanagement.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message, String s) {
        super(message);
    }
}