package org.example.ordermanagement.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.exception.BusinessException;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.OrderRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
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
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "Không tìm thấy khách hàng này"));

        Order order = Order.builder()

                .orderCode(request.getOrderCode())
                .status(OrderStatus.CREATED)
                .createdBy(user)
                .totalAmount(request.getTotalAmount())
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
                        .customerName(order.getCreatedBy() != null ? order.getCreatedBy().getUsername() : "N/A")
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
    private String getCurrentUsername() {
        org.springframework.security.core.Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("UNAUTHORIZED", "Bạn cần đăng nhập để thực hiện thao tác này");
        }
        return authentication.getName();
    }
    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Đơn hàng không tồn tại"));

        String currentUsername = getCurrentUsername();

        // Lấy danh sách quyền của User hiện tại
        var authorities = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList();

        // Ràng buộc: Nếu KHÔNG phải Admin/Staff VÀ KHÔNG phải người tạo đơn -> Chặn
        boolean isAdminOrStaff = authorities.contains("ADMIN") || authorities.contains("STAFF");
        boolean isOwner = order.getCreatedBy().getUsername().equals(currentUsername);

        if (!isAdminOrStaff && !isOwner) {
            throw new BusinessException("ACCESS_DENIED", "Bạn không có quyền xem đơn hàng của người khác");
        }

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .customerName(order.getCreatedBy().getUsername())
                .build();
    }



    // Triển khai trong OrderServiceImpl.java
    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Đơn hàng không tồn tại"));

        // Ràng buộc nghiệp vụ: STAFF không được sửa đơn đã CANCELLED
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("INVALID_STATUS_CHANGE", "Không thể cập nhật đơn hàng đã bị hủy");
        }

        order.setStatus(newStatus);
        order = orderRepository.save(order);

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus())
                .customerName(order.getCreatedBy().getUsername())
                .build();
    }
    @Override
    public List<OrderResponse> getOrdersByCurrentUser() {
        // 1. Lấy username của người đang đăng nhập từ Security Context
        String currentUsername = getCurrentUsername();

        // 2. Tìm danh sách đơn hàng dựa trên username người tạo
        // Giả sử bạn đã có hàm findByCreatedBy_Username trong OrderRepository
        List<Order> orders = orderRepository.findByCreatedBy_Username(currentUsername);

        // 3. Map sang OrderResponse để trả về cho Client
        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .orderCode(order.getOrderCode())
                        .status(order.getStatus())
                        .customerName(order.getCreatedBy().getUsername())
                        .build())
                .toList();
    }
}