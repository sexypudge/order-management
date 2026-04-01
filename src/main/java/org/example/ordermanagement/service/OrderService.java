package org.example.ordermanagement.service;

import org.example.ordermanagement.model.dto.request.OrderCreateRequest;
import org.example.ordermanagement.model.dto.request.OrderHistoryRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse getOrderById(Long id);
    PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest orderSearchRequest);
    boolean isOwner(Long orderId);
    OrderResponse confirmStatus(Long orderId);
    OrderResponse cancelStatus(Long orderId);
    PageResponse<OrderResponse> getOrderHistory(Long id, int page, int size, String sortBy, String sortDirection, OrderHistoryRequest request);

}
