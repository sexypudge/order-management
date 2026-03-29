package org.example.ordermanagement.service;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.dto.request.OrderRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest request);
    OrderResponse updateStatus(Long id, OrderStatus newStatus);
    List<OrderResponse> getOrdersByCurrentUser();
}