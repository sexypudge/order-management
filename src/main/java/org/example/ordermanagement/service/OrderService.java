package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import java.util.List;
public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    List<OrderResponse> getOrders();
}