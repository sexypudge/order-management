package org.example.ordermanagement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    USER_EXISTED(1001, "User existed"),
    ROLE_NOT_FOUND(1002, "Role not found"),
    INVALID_KEY(1003, "Invalid Key"),;

    private int code;
    private String message;
}
