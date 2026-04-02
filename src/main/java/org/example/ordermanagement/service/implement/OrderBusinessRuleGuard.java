package org.example.ordermanagement.service.implement;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class OrderBusinessRuleGuard {

    public void validateUpdate(Order order, OrderStatus newStatus,
                               boolean isAdmin, boolean isStaff, boolean isCustomer) {

        if (isCustomer) {
            throw new AccessDeniedException("CUSTOMER cannot update order status");
        }

        if (!isAdmin && !isStaff) {
            throw new AccessDeniedException("You are not allowed to update order status");
        }

        OrderStatus oldStatus = order.getStatus();

        if (oldStatus == OrderStatus.COMPLETED) {
            throw new BusinessException("ORDER_COMPLETED_LOCKED",
                    "COMPLETED order cannot be updated");
        }

        if (isStaff && oldStatus == OrderStatus.CANCELLED) {
            throw new BusinessException("ORDER_CANCELLED_CANNOT_UPDATE",
                    "STAFF cannot update CANCELLED order");
        }

        if (oldStatus == OrderStatus.CANCELLED) {
            if (!isAdmin) {
                throw new BusinessException("ORDER_CANCELLED_ADMIN_ONLY",
                        "Only ADMIN can update CANCELLED order");
            }
            if (newStatus != OrderStatus.PROCESSING) {
                throw new BusinessException("ORDER_CANCELLED_ONLY_PROCESSING",
                        "CANCELLED order can only be updated to PROCESSING");
            }
        }

        ensureValidTransition(oldStatus, newStatus, isAdmin);
    }

    private void ensureValidTransition(OrderStatus oldStatus, OrderStatus newStatus, boolean isAdmin) {
        boolean ok = false;

        if (oldStatus == OrderStatus.CREATED && newStatus == OrderStatus.PROCESSING) ok = true;
        if (oldStatus == OrderStatus.PROCESSING && newStatus == OrderStatus.COMPLETED) ok = true;
        if (oldStatus == OrderStatus.CREATED && newStatus == OrderStatus.CANCELLED) ok = true;
        if (oldStatus == OrderStatus.CANCELLED && newStatus == OrderStatus.PROCESSING && isAdmin) ok = true;

        if (!ok) {
            throw new BusinessException("INVALID_STATUS_TRANSITION",
                    "Invalid transition: " + oldStatus + " -> " + newStatus);
        }
    }
}