package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.OrderCreateRequest;
import org.example.ordermanagement.model.dto.request.RoleRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.RoleResponse;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
}
