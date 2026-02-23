package org.example.ordermanagement.exception;

import org.example.ordermanagement.common.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Map<String, Object> handleBusiness(BusinessException ex) {
        return ResponseUtil.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleOther(Exception ex) {
        return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
    }
}