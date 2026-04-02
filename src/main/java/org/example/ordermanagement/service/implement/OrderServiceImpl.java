package org.example.ordermanagement.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.OrderHistory;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.CreateOrderRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.response.*;
import org.example.ordermanagement.repository.OrderHistoryRepository;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderBusinessRuleGuard orderBusinessRuleGuard;

    // NEW: audit repo
    private final OrderHistoryRepository orderHistoryRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        if (orderRepository.existsByOrderCodeIgnoreCase(request.getOrderCode())) {
            throw new BusinessException("ORDER_CODE_EXISTS", "Order code already exists");
        }

        // lấy user hiện tại từ SecurityContext
        String currentUsername = currentUsername();
        if (currentUsername == null) {
            throw new BusinessException("UNAUTHORIZED", "Unauthorized");
        }

        User createdBy;

        if (hasRole("ROLE_ADMIN") && request.getCreatedByUserId() != null) {
            createdBy = userRepository.findById(request.getCreatedByUserId())
                    .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));
        } else {
            createdBy = userRepository.findByUsernameIgnoreCase(currentUsername)
                    .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));
        }

        Order order = Order.builder()
                .orderCode(request.getOrderCode())
                .totalAmount(request.getTotalAmount())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .build();

        Order saved = orderRepository.save(order);

        // AUDIT: create order (oldStatus = null)
        saveHistory(saved.getId(), null, OrderStatus.CREATED, currentUsername);

        // LOG
        log.info("CREATE_ORDER orderId={}, orderCode={}, newStatus={}, updatedBy={}",
                saved.getId(), saved.getOrderCode(), saved.getStatus(), currentUsername);

        return toResponse(saved);
    }

    @Override
    public List<OrderResponse> getOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new BusinessException("UNAUTHORIZED", "Unauthorized");
        }

        String username = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isStaff = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));

        List<Order> orders;

        if (isAdmin || isStaff) {
            orders = orderRepository.findAll();
        } else {
            // CUSTOMER: chỉ xem order của mình
            orders = orderRepository.findByCreatedByUsername(username);
        }

        return orders.stream().map(this::toResponse).toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new BusinessException("UNAUTHORIZED", "Unauthorized");
        }
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            throw new BusinessException("ORDER_NOT_FOUND", "Order not found");
        }
        Order order = orderOpt.get();
        if (hasRole("ROLE_CUSTOMER")) {
            String username = auth.getName();
            if (order.getCreatedBy() == null || order.getCreatedBy().getUsername() == null
                    || !order.getCreatedBy().getUsername().equalsIgnoreCase(username)) {
                throw new AccessDeniedException("CUSTOMER cannot view other user's order");
            }
        }
        return toResponse(order);
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
    public PageResponse<OrderSearchResponse> searchOrders(OrderSearchRequest request,
                                                          int page,
                                                          int size,
                                                          String sortBy,
                                                          String sortDirection) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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

        if (orderCode != null && orderCode.isBlank()) orderCode = null;
        if (username != null && username.isBlank()) username = null;

        if (hasRole("ROLE_CUSTOMER")) {
            username = auth.getName();
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
    public OrderResponse assignStatusToOrder(Long orderId, OrderStatus newStatus) {

        boolean isAdmin = hasRole("ROLE_ADMIN");
        boolean isStaff = hasRole("ROLE_STAFF");
        boolean isCustomer = hasRole("ROLE_CUSTOMER");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Order not found"));

        OrderStatus oldStatus = order.getStatus();

        orderBusinessRuleGuard.validateUpdate(order, newStatus, isAdmin, isStaff, isCustomer);

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        String updatedBy = currentUsername();
        saveHistory(saved.getId(), oldStatus, newStatus, updatedBy);

        log.info("UPDATE_ORDER_STATUS orderId={}, oldStatus={}, newStatus={}, updatedBy={}",
                saved.getId(), oldStatus, newStatus, updatedBy);

        return toResponse(saved);
    }
    private void saveHistory(Long orderId, OrderStatus oldStatus, OrderStatus newStatus, String updatedBy) {
        if (updatedBy == null) updatedBy = "UNKNOWN";

        OrderHistory history = OrderHistory.builder()
                .orderId(orderId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .updatedBy(updatedBy)
                .updatedAt(LocalDateTime.now())
                .build();

        orderHistoryRepository.save(history);
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? null : auth.getName();
    }

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }
}