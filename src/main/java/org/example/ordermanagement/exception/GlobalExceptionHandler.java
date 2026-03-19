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
                .code(String.valueOf(ErrorCode.UNCATEGORIZED_ERROR.getCode()))
                .message(ErrorCode.UNCATEGORIZED_ERROR.getMessage())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // lỗi nghiệp vụ
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(String.valueOf(errorCode.getCode()))
                .message(errorCode.getMessage())
                .build();


        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }
}