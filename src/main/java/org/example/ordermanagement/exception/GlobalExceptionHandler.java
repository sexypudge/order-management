package org.example.ordermanagement.exception;

import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>>HandleRuntimeException(RuntimeException exception){
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
}
