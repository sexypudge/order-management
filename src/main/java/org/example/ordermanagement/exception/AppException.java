package org.example.ordermanagement.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private String errorCode;

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}