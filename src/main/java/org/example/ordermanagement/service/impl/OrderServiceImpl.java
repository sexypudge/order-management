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
        String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

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
                .totalAmount(order.getTotalAmount())
                .customerName(user.getUsername())
                .build();
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

// lấy thông tin người đang đăng nhập từ Token SecurityContext
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); //  username lấy từ JWT

        // lấy danh sách Role để check xem có phải Staff/Admin không
        boolean isAdminOrStaff = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_STAFF"));

        // nếu k phải Staff/Admin VÀ đơn hàng này KHÔNG THUỘC về User hiện tại thì bị chặn
        if (!isAdminOrStaff && !order.getUser().getUsername().equals(currentUsername)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .customerName(order.getUser().getUsername())
                .build();
    }

    @Override
    public PageResponse<OrderResponse> searchOrders(int page, int size, String sortBy, String sortDirection, OrderSearchRequest request) {
        // 1. Lấy thông tin User đang đăng nhập
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Check quyền STAFF/ADMIN
        boolean isStaffOrAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF") || a.getAuthority().equals("ROLE_ADMIN"));

        String searchOrderCode = (request != null) ? request.getOrderCode() : null;
        OrderStatus searchStatus = (request != null) ? request.getStatus() : null;

        // nếu không phải STAFF, thì ép username tìm kiếm là của chính mình
        String searchUsername;
        if (isStaffOrAdmin) {
            searchUsername = (request != null) ? request.getUsername() : null;
        } else {
            searchUsername = currentUsername;
        }

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);



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
    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }
    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .customerName(order.getUser() != null ? order.getUser().getUsername() : "N/A")
                .build();
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new AppException(ErrorCode.ORDER_CANCELLED);
        }

        order.setStatus(newStatus);

        return mapToOrderResponse(orderRepository.save(order));
    }
    @Override
    public List<OrderResponse> getMyOrders() {
        // lấy username của người đang đăng nhập từ SecurityContext
        String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        //  Tìm danh sách đơn hàng của User này

        List<Order> orders = orderRepository.findAllByUser(user);

        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }
}