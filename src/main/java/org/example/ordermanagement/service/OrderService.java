package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;

import java.util.List;
public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    List<OrderResponse> getOrders();
    OrderResponse getOrderById(Long id);
}