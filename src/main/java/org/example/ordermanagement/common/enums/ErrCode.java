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
    ACCESS_DENIED(1009, "do not have permission", HttpStatus.FORBIDDEN),
    INVALID_STATUS_TRANSITION(1012, "Invalid state conversion!", HttpStatus.BAD_REQUEST),
    INVALID_STATUS( 1011, "Invalid order status.", HttpStatus.BAD_REQUEST),
    ORDER_COMPLETED_LOCKED(1013, "Completed order cannot be updated", HttpStatus.BAD_REQUEST),
    ORDER_CANCELLED_CANNOT_UPDATE(1014, "Staff cannot update cancelled order", HttpStatus.BAD_REQUEST),
    ORDER_CANCELLED_ADMIN_ONLY(1015, "Only admin can update cancelled order", HttpStatus.FORBIDDEN),
    ORDER_CANCELLED_ONLY_PROCESSING(1016, "Cancelled order can only move to processing", HttpStatus.BAD_REQUEST),
    CUSTOMER_CANNOT_UPDATE(1017, "Customer cannot update order", HttpStatus.FORBIDDEN),
    CAN_NOT_UPDATE_ORDER_STATUS(1010,"Order cancelled! can not update!", HttpStatus.BAD_REQUEST);
    ;

    private int code;
    private String message;
    private HttpStatus statusCode;
}
