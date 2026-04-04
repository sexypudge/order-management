package org.example.ordermanagement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1002, "Role not found", HttpStatus.NOT_FOUND),
    INVALID_KEY(1003, "Invalid Key",HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1004,"Role existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"USER_NOT_EXISTED",HttpStatus.NOT_FOUND),
    INVALID_PAGE_NUMBER(1006, "Page number must be zero or greater",HttpStatus.BAD_REQUEST),
    INVALID_PAGE_SIZE(1007, "Page size must be between 1 and 100", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1008,"Order not existed!", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION(1012, "Invalid state conversion!", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1009, "do not have permission", HttpStatus.FORBIDDEN);

    private int code;
    private String message;
    private HttpStatus statusCode;
}
