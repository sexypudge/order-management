package org.example.ordermanagement.exception;

import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseUtil.error(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = "INVALID_REQUEST";
        var errors = ex.getBindingResult().getFieldErrors();
        if (!errors.isEmpty()) {
            message = errors.get(0).getDefaultMessage();
        }
        return ResponseEntity.badRequest()
                .body(ResponseUtil.error("INVALID_REQUEST", message));
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleHandlerValidation(HandlerMethodValidationException ex) {
        String message = "INVALID_REQUEST";
        var errors = ex.getAllErrors();
        if (!errors.isEmpty()) {
            message = errors.get(0).getDefaultMessage();
        }
        return ResponseEntity.badRequest()
                .body(ResponseUtil.error("INVALID_REQUEST", message));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidEnum(HttpMessageNotReadableException ex) {
        String message = "Invalid request";
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("OrderStatus")) {
                message = "Status must be CREATED, CONFIRMED, CANCELLED";
            } else if (ex.getMessage().contains("UserStatus")) {
                message = "Status must be ACTIVE, INACTIVE";
            }
        }
        return ResponseEntity.badRequest()
                .body(ResponseUtil.error("INVALID_REQUEST", message));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.error("INTERNAL_SERVER_ERROR", ex.getMessage()));
    }
}