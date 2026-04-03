package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.OrderHistory;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.OrderCreateRequest;
import org.example.ordermanagement.model.dto.request.OrderHistoryRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.request.UpdateOrderStatusRequest;
import org.example.ordermanagement.model.dto.response.OrderHistoryResponse;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.OrderHistoryRepository;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderBusinessRuleGuard orderBusinessRuleGuard;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("START - User [{}] is creating a new order", currentUser);

        User user = userRepository.findById(orderCreateRequest.getUserId()).orElseThrow(()
                -> new AppException(ErrCode.USER_NOT_EXISTED));
        try {
            Order order = new Order();
            order.setOrderCode("ORD-" + System.currentTimeMillis());
            order.setStatus(OrderStatus.CREATED);
            order.setTotalAmount(orderCreateRequest.getTotalAmount());
            order.setCreatedAt(LocalDateTime.now());
            order.setCreatedBy(user);

            orderRepository.save(order);
            saveHistory(order, null, OrderStatus.CREATED, currentUser);
            log.info("SUCCESS - Order created successfully. ID: {}, Code: {}, CreatedBy: {}",
                    order.getId(), order.getOrderCode(), currentUser);

            return responseDTO(order);
        } catch (Exception e) {
            log.error("ERROR - Failed to create order for user [{}]. Reason: {}", currentUser, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest orderSearchRequest) {

        String actualField;
        if (sortBy.equals("name")) {
            actualField = "createdBy.username";
        } else {
            actualField = sortBy;
        }

        Sort sort;
        if (sortDirection.equalsIgnoreCase("ASC")) {
            sort = Sort.by(actualField).ascending();
        } else {
            sort = Sort.by(actualField).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Long searchId = null;
        if (orderSearchRequest != null) {
            searchId = orderSearchRequest.getId();
        }

        String searchName = null;
        if (orderSearchRequest != null) {
            if (orderSearchRequest.getName() != null && !orderSearchRequest.getName().isEmpty()) {
                searchName = orderSearchRequest.getName();
            }
        }

        OrderStatus searchStatus = null;
        if (orderSearchRequest != null) {
            searchStatus = orderSearchRequest.getStatus();
        }

        String searchOrderCode = null;
        if (orderSearchRequest != null) {
            searchOrderCode = orderSearchRequest.getOrderCode();
        }

        Page<Order> orderPage = orderRepository.searchOrderBasics(searchId, searchName, searchStatus, searchOrderCode, pageable);

        return PageResponse.<OrderResponse>builder()
                .content(orderPage.getContent().stream()
                        .map(this::responseDTO)
                        .toList())
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

    @Override
    @Transactional
    public OrderResponse getOrderById(Long id, String username) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrCode.ORDER_NOT_FOUND));
        return responseDTO(order);
    }

    private OrderResponse responseDTO(Order order) {
        UserResponse user = UserResponse.builder()
                .id(order.getCreatedBy().getId())
                .username(order.getCreatedBy().getUsername())
                .status(order.getCreatedBy().getStatus().name())
                .roles(order.getCreatedBy().getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .createdBy(user)
                .build();
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.info("START - User [{}] is updating Order ID: {} to Status: {}", currentUser, orderId, request.getStatus());

        boolean isAdmin = hasRole("ROLE_ADMIN");
        boolean isStaff = hasRole("ROLE_STAFF");
        boolean isCustomer = hasRole("ROLE_CUSTOMER");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("FAILED - Order ID: {} not found. UpdatedBy: {}", orderId, currentUser);
                    return new AppException(ErrCode.ORDER_NOT_FOUND);
                });

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        orderBusinessRuleGuard.validateUpdate(order, newStatus, isAdmin, isStaff, isCustomer);

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        saveHistory(order, oldStatus, newStatus, currentUser);
        log.info("SUCCESS - Order ID: {} updated from [{}] to [{}] by User: {}", orderId, oldStatus, newStatus, currentUser);
        return responseDTO(saved);
    }


    @Override
    @Transactional
    public PageResponse<OrderHistoryResponse> getOrderHistory(Long id, int page, int size, String sortBy, String sortDirection, OrderHistoryRequest request) {

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        OrderStatus searchStatus = null;
        if (request != null && request.getStatus() != null && !request.getStatus().isEmpty()) {
            try {
                searchStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
            } catch (Exception e) {
                throw new AppException(ErrCode.INVALID_STATUS);
            }
        }
        String searchUpdatedBy = (request != null) ? request.getCreatedBy() : null;

        Page<OrderHistory> historyPage = orderHistoryRepository.findWithFilters(id, searchStatus, searchUpdatedBy, pageable);

        List<OrderHistoryResponse> content = historyPage.getContent().stream()
                .map(h -> OrderHistoryResponse.builder()
                        .id(h.getId())
                        .orderId(h.getOrder().getId())
                        .oldStatus(h.getOldStatus() != null ? h.getOldStatus().name() : "START")
                        .newStatus(h.getNewStatus().name())
                        .updatedBy(h.getUpdatedBy())
                        .updatedAt(h.getUpdatedAt())
                        .build())
                .toList();

        return PageResponse.<OrderHistoryResponse>builder()
                .content(content)
                .totalElements(historyPage.getTotalElements())
                .totalPages(historyPage.getTotalPages())
                .page(historyPage.getNumber())
                .size(historyPage.getSize())
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

    }
    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private void saveHistory(Order order, OrderStatus oldStatus, OrderStatus newStatus, String updatedBy) {
        OrderHistory history = OrderHistory.builder()
                .order(order)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .updatedBy(updatedBy != null ? updatedBy : "UNKNOWN")
                .updatedAt(LocalDateTime.now())
                .build();
        orderHistoryRepository.save(history);
    }

}
