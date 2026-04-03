package org.example.ordermanagement.service;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.dto.request.OrderCreateRequest;
import org.example.ordermanagement.model.dto.request.OrderHistoryRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.request.UpdateOrderStatusRequest;
import org.example.ordermanagement.model.dto.response.OrderHistoryResponse;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse getOrderById(Long id, String username);
    PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest orderSearchRequest);


    PageResponse<OrderHistoryResponse> getOrderHistory(Long id, int page, int size, String sortBy, String sortDirection, OrderHistoryRequest request);
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

}
