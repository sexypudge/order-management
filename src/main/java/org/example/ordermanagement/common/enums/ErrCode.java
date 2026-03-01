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
    USER_NOT_EXISTED(1005,"USER_NOT_EXISTED",HttpStatus.NOT_FOUND);

    private int code;
    private String message;
    private HttpStatus statusCode;
}
