package org.example.ordermanagement.exception;

import org.example.ordermanagement.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // looix hheej thống
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handlingRuntimeException(RuntimeException exception) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code("UNCATEGORIZED_ERROR")
                .message("Có lỗi hệ thống: " + exception.getMessage())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // lỗi nghiệp vụ
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAppException(AppException exception) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(exception.getErrorCode())
                .message(exception.getMessage())
                .build();


        return ResponseEntity.ok(apiResponse);
    }
}