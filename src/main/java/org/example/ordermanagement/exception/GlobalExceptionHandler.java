package org.example.ordermanagement.exception;

import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrCode.ACCESS_DENIED.getCode());
        apiResponse.setMessage(ErrCode.ACCESS_DENIED.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>>HandleRuntimeException(Exception exception){
        exception.printStackTrace();
        ApiResponse<?> apiResponse= new ApiResponse<>();
        apiResponse.setCode(ErrCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<?>>HandleAppException(AppException exception){
        ApiResponse<?> apiResponse = new ApiResponse();
        ErrCode errCode = exception.getErrCode();
        apiResponse.setCode(errCode.getCode());
        apiResponse.setMessage(errCode.getMessage());
        return ResponseEntity.status(errCode.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getBindingResult().getFieldError().getDefaultMessage();

        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(400);
        apiResponse.setMessage(enumKey);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
