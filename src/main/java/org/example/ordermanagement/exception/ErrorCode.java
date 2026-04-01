package org.example.ordermanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Mật khẩu không đúng hoặc không có quyền", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(1002, "Uncategorized error", HttpStatus.BAD_REQUEST),

    ORDER_NOT_FOUND(1007, "Đơn hàng không tồn tại", HttpStatus.NOT_FOUND),
    ORDER_CANCELLED(1008, "Đơn hàng đã hủy, không thể cập nhật", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1009, "Bạn không có quyền thực hiện hành động này", HttpStatus.FORBIDDEN),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}