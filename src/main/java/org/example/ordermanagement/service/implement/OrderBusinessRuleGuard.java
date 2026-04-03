package org.example.ordermanagement.service.implement;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.domain.Order;


import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.exception.AppException;
import org.springframework.stereotype.Component;

@Component
public class OrderBusinessRuleGuard {

    public void validateUpdate(Order order, OrderStatus newStatus,
                               boolean isAdmin, boolean isStaff, boolean isCustomer) {

        if (isCustomer) {
            throw new AppException(ErrCode.CUSTOMER_CANNOT_UPDATE);
        }

        if (!isAdmin && !isStaff) {
            throw new AppException(ErrCode.ACCESS_DENIED);
        }

        OrderStatus oldStatus = order.getStatus();

        if (oldStatus == OrderStatus.COMPLETED) {
            throw new AppException(ErrCode.ORDER_COMPLETED_LOCKED);
        }

        if (isStaff && oldStatus == OrderStatus.CANCELLED) {
            throw new AppException(ErrCode.ORDER_CANCELLED_CANNOT_UPDATE);
        }

        if (oldStatus == OrderStatus.CANCELLED) {
            if (!isAdmin) {
                throw new AppException(ErrCode.ORDER_CANCELLED_ADMIN_ONLY);
            }
            if (newStatus != OrderStatus.PROCESSING) {
                throw new AppException(ErrCode.ORDER_CANCELLED_ONLY_PROCESSING);
            }
        }

        ensureValidTransition(oldStatus, newStatus, isAdmin);
    }
    private void ensureValidTransition(OrderStatus oldStatus, OrderStatus newStatus, boolean isAdmin) {
        boolean isValid = false;

        if (oldStatus == OrderStatus.CREATED) {
            if (newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED) {
                isValid = true;
            }
        } else if (oldStatus == OrderStatus.PROCESSING) {
            if (newStatus == OrderStatus.COMPLETED) {
                isValid = true;
            }
        }

        if (!isValid) {
            throw new AppException(ErrCode.INVALID_STATUS_TRANSITION);

        }
    }
}
