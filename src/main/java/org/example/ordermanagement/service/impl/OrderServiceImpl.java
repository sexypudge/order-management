package org.example.ordermanagement.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.domain.Order;
import org.example.ordermanagement.domain.User;
import org.example.ordermanagement.dto.request.OrderRequest;
import org.example.ordermanagement.dto.request.OrderSearchRequest;
import org.example.ordermanagement.dto.response.OrderResponse;
import org.example.ordermanagement.dto.response.PageResponse;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.exception.ErrorCode;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.valueOf("USER_NOT_FOUND")));

        Order order = Order.builder()
                .orderCode(request.getOrderCode())
                .status(OrderStatus.CREATED)
                .user(user)
                .build();

        order = orderRepository.save(order);

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .customerName(user.getUsername())
                .build();
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.valueOf("ORDER_NOT_FOUND")));


        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .customerName(order.getUser().getUsername())
                .build();
    }

    @Override
    public PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest request) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String searchOrderCode = (request != null) ? request.getOrderCode() : null;
        String searchUsername = (request != null) ? request.getUsername() : null;
        OrderStatus searchStatus = (request != null) ? request.getStatus() : null;

        Page<Order> orderPage = orderRepository.searchOrders(searchOrderCode, searchUsername, searchStatus, pageable);

        List<OrderResponse> content = orderPage.getContent().stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .orderCode(order.getOrderCode())
                        .status(order.getStatus())
                        .customerName(order.getUser() != null ? order.getUser().getUsername() : "N/A")
                        .build())
                .toList();



        return PageResponse.<OrderResponse>builder()
                .content(content)
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .hasNext(orderPage.hasNext())
                .hasPrevious(orderPage.hasPrevious())
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
    }
}