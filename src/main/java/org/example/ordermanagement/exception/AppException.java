package org.example.ordermanagement.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    // 1. Đổi từ String sang ErrorCode để khớp với tham số truyền vào
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        // 2. Gọi constructor của lớp cha (RuntimeException) và truyền message vào
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}