package org.example.ordermanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.OrderStatus;

import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Order;

import org.example.ordermanagement.model.domain.Role;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.response.OrderSearchResponse;
import org.example.ordermanagement.model.dto.response.*;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        if (orderRepository.existsByOrderCodeIgnoreCase(request.getOrderCode())) {
            throw new BusinessException("ORDER_CODE_EXISTS", "Order code already exists");
        }

        User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));

        Order order = Order.builder()
                .orderCode(request.getOrderCode())
                .totalAmount(request.getTotalAmount())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .createdBy(user)
                .build();

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }
    @Override
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> result = new ArrayList<>();

        for (Order o : orders) {
            result.add(toResponse(o));
        }
        return result;
    }
    @Override
    public OrderResponse getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new BusinessException("ORDER_NOT_FOUND", "Order not found");
        }
        return toResponse(order.get());
    }
    private OrderResponse toResponse(Order order) {
        User u = order.getCreatedBy();
        return new OrderResponse(
                order.getId(),
                order.getOrderCode(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                new UserOrderResponse(u.getId(), u.getUsername())
        );
    }


    @Override
    public PageResponse<OrderSearchResponse> searchOrders(OrderSearchRequest request, int page, int size, String sortBy, String sortDirection) {
        String orderCode = null;
        String username = null;
        OrderStatus status = null;

        if (request != null) {
            orderCode = request.getOrderCode();
            username = request.getUsername();
            if (username != null && username.isBlank()) username = null;
            String statusStr = request.getStatus();
            if (statusStr != null && !statusStr.isBlank()) {
                try {
                    status = OrderStatus.valueOf(statusStr.trim().toUpperCase());
                } catch (Exception e) {
                    throw new BusinessException("INVALID_REQUEST", "status must be CREATED,CONFIRMED,CANCELLED");
                }
            }
        }

        if (page < 0) {
            throw new BusinessException("INVALID_REQUEST", "Page must be >= 0");
        }
        if (sortBy == null || sortBy.isBlank()) sortBy = "orderCode";
        if (sortDirection == null || sortDirection.isBlank()) sortDirection = "ASC";

        String sortField;
        if (sortBy.equalsIgnoreCase("orderCode")) {
            sortField = "orderCode";
        } else if (sortBy.equalsIgnoreCase("username")) {
            sortField = "createdBy.username";
        } else if (sortBy.equalsIgnoreCase("status")) {
            sortField = "status";
        } else {
            throw new BusinessException("INVALID_REQUEST", "sortBy must be orderCode, username, or status");
        }

        Sort.Direction direction;
        if (sortDirection.equalsIgnoreCase("ASC")) {
            direction = Sort.Direction.ASC;
        } else if (sortDirection.equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        } else {
            throw new BusinessException("INVALID_REQUEST", "sortDirection must be ASC or DESC");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));



        Page<Order> orderPage = orderRepository.searchOrders(orderCode, username, status, pageable);

        List<OrderSearchResponse> content = new ArrayList<>();
        for (Order o : orderPage.getContent()) {
            content.add(new OrderSearchResponse(
                    o.getId(),
                    o.getOrderCode(),
                    o.getStatus(),
                    o.getTotalAmount(),
                    o.getCreatedAt(),
                    o.getCreatedBy().getUsername()
            ));
        }

        PageResponse<OrderSearchResponse> res = new PageResponse<>();
        res.setContent(content);
        res.setPage(orderPage.getNumber());
        res.setSize(orderPage.getSize());
        res.setTotalElements(orderPage.getTotalElements());
        res.setTotalPages(orderPage.getTotalPages());
        res.setHasNext(orderPage.hasNext());
        res.setHasPrevious(orderPage.hasPrevious());
        res.setSortBy(sortBy.toLowerCase());
        res.setSortDirection(direction.name());

        return res;
    }



}