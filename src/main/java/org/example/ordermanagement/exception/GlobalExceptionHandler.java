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
    ResponseEntity<ApiResponse>HandleRuntimeException(RuntimeException exception){
        ApiResponse apiResponse= new ApiResponse<>();
        apiResponse.setResCode(ErrCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse>HandleAppException(AppException exception){
        ApiResponse apiResponse = new ApiResponse();
        ErrCode errCode = exception.getErrCode();
        apiResponse.setResCode(errCode.getCode());
        apiResponse.setMessage(errCode.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
