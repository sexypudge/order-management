package org.example.ordermanagement.exception;

import lombok.Getter;
import org.example.ordermanagement.common.enums.ErrCode;
@Getter
public class AppException extends RuntimeException{
    private ErrCode errCode;

    AppException (ErrCode errCode){
        super(errCode.getMessage());
        this.errCode =errCode;
    }
}
