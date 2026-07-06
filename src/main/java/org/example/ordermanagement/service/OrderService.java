package org.example.ordermanagement.service;

import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.request.OrderHistorySearchRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.response.*;

import java.util.List;
public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
//    List<OrderResponse> getOrders();
//    OrderResponse getOrderById(Long id);
    OrderResponse assignStatusToOrder(Long orderId, OrderStatus status);
    PageResponse<OrderSearchResponse> searchOrders(OrderSearchRequest request,
                                                   int page,
                                                   int size,
                                                   String sortBy,
                                                   String sortDirection);
    PageResponse<OrderHistoryResponse> getOrderHistory(
            Long orderId,
            OrderHistorySearchRequest request,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
}