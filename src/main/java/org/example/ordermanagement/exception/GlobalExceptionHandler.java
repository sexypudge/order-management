package org.example.ordermanagement.exception;

import org.example.ordermanagement.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //  Giúp bắt lỗi của tất cả Controller
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ApiResponse<Object> handleAppException(AppException e) {
        return ApiResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

}