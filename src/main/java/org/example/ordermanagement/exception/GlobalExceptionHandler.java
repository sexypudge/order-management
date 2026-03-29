package org.example.ordermanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Validation Error
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            ValidationException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(java.time.LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDATION_ERROR")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    // 2. Business Error
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(java.time.LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("BUSINESS_ERROR")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    // 3. Access Denied
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(java.time.LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("ACCESS_DENIED")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    // 4. Catch all
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(java.time.LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("INTERNAL_SERVER_ERROR")
                .message("Unexpected error occurred")
                .path(request.getRequestURI())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code("INVALID_INPUT")
                .message(message)
                .build());
    }
}