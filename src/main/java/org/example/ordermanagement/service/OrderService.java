package org.example.ordermanagement.service;

import org.example.ordermanagement.dto.request.OrderRequest;
import org.example.ordermanagement.dto.request.OrderSearchRequest;
import org.example.ordermanagement.dto.response.OrderResponse;
import org.example.ordermanagement.dto.response.PageResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
//    OrderResponse getOrderById(Long id);
//    PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest request);

}