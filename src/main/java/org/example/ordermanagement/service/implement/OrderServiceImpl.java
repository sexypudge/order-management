package org.example.ordermanagement.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.common.enums.ErrCode;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.common.enums.UserStatus;
import org.example.ordermanagement.exception.AppException;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.OrderCreateRequest;
import org.example.ordermanagement.model.dto.request.OrderHistoryRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.request.UpdateOrderStatusRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.repository.OrderRepository;
import org.example.ordermanagement.repository.UserRepository;
import org.example.ordermanagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        User user = userRepository.findById(orderCreateRequest.getUserId()).orElseThrow(() -> new AppException(ErrCode.USER_NOT_EXISTED));

        Order order = new Order();
        order.setOrderCode("ORD-" + System.currentTimeMillis());
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(orderCreateRequest.getTotalAmount());
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedBy(user);

        orderRepository.save(order);

        return responseDTO(order);
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

        Page<Order> orderPage = orderRepository.searchOrderBasics(searchId, searchName, searchStatus,searchOrderCode, pageable);

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
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isStaff = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));
        boolean isCustomer = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrCode.ORDER_NOT_FOUND));

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();


        boolean isValid = false;

        if (oldStatus == OrderStatus.CREATED) {
            if (newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED) {
                isValid = true;
            }
        } else if (oldStatus == OrderStatus.PROCESSING) {
            if (newStatus == OrderStatus.COMPLETED) {
                isValid = true;
            }
        }

        if (!isValid) {
            throw new AppException(ErrCode.INVALID_STATUS_TRANSITION);

        }


        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);


        return responseDTO(saved);
    }

    @Override
    @Transactional
    public PageResponse<OrderResponse> getOrderHistory(Long id, int page, int size, String sortBy, String sortDirection, OrderHistoryRequest request) {
        if (page < 0) {
            throw new AppException(ErrCode.INVALID_PAGE_NUMBER);
        }
        if (size <= 0 || size > 100) {
            throw new AppException(ErrCode.INVALID_PAGE_SIZE);
        }

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);


        OrderStatus searchStatus = null;
        if (request != null && request.getStatus() != null && !request.getStatus().isEmpty()) {
            try {
                searchStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrCode.INVALID_STATUS);
            }
        }


        String searchCreatedBy = (request != null) ? request.getCreatedBy() : null;

        Long searchOrderId = (id != null) ? id : (request != null ? request.getOrderId() : null);


        Page<Order> orderPage = orderRepository.searchOrderBasics(
                searchOrderId,
                searchCreatedBy,
                searchStatus,
                null,
                pageable
        );

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

}
