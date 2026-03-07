package org.example.ordermanagement.exception;

import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseUtil.error(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.error("INTERNAL_SERVER_ERROR", ex.getMessage()));
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
}