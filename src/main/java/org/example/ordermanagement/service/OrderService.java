package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);

}