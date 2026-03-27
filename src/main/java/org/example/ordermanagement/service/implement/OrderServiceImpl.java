package org.example.ordermanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.OrderStatus;

import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Order;

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

import org.springframework.security.core.context.SecurityContextHolder;
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

        var auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isStaff = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));

        List<Order> orders;

        if (isAdmin || isStaff) {
            orders = orderRepository.findAll();
        }
        else {
            orders = orderRepository.findByCreatedByUsername(username);
        }
        return orders.stream()
                .map(this::toResponse)
                .toList();
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
    public PageResponse<OrderSearchResponse> searchOrders(OrderSearchRequest request, int page, int size, String sortBy, String sortDirection
    ) {
        String sortField;
        if (sortBy.equalsIgnoreCase("orderCode")) {
            sortField = "orderCode";
        } else if (sortBy.equalsIgnoreCase("username")) {
            sortField = "createdBy.username";
        } else {
            sortField = "status";
        }

        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        String orderCode = request != null ? request.getOrderCode() : null;
        String username = request != null ? request.getUsername() : null;
        OrderStatus status = request != null ? request.getStatus() : null;

        if (username != null && username.isBlank()) {
            username = null;
        }

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
    @Override
    @Transactional
    public OrderResponse assignStatusToOrder(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("NOT_FOUND", "Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("INVALID_REQUEST", "Cannot update cancelled order");
        }
        order.setStatus(status);
        Order saved = orderRepository.save(order);

        return toResponse(saved);
    }
}