package org.example.ordermanagement.controller;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.model.dto.request.OrderRequest;
import org.example.ordermanagement.model.dto.request.OrderSearchRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.OrderResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/{create}")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request){
        var result = orderService.createOrder(request);
        return ApiResponse.<OrderResponse>builder()
                .code("success")
                .message("Tạo đơn hàng thành công")
                .data(result)
                .build();
    }
    @GetMapping("/my-orders")
    public ApiResponse<List<OrderResponse>> getMyOrders() {
        // Logic này thường sẽ lấy username từ SecurityContextHolder
        // Sau đó gọi service để tìm các đơn có createdBy là username đó.
        var result = orderService.getOrdersByCurrentUser();
        return ApiResponse.<List<OrderResponse>>builder()
                .code("SUCCESS")
                .data(result)
                .build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long orderId) {
        var result = orderService.getOrderById(orderId);

        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder()
                .code("SUCCESS")
                .data(result)
                .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> searchOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestBody(required = false) OrderSearchRequest request) {

        var result = orderService.searchOrders(page, size, sortBy, sortDirection, request);

        return ResponseEntity.ok(ApiResponse.<PageResponse<OrderResponse>>builder()
                .code("SUCCESS")
                .data(result)
                .build());
    }
    @PutMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestParam org.example.ordermanagement.common.enums.OrderStatus status) {

        var result = orderService.updateStatus(orderId, status);

        return ApiResponse.<OrderResponse>builder()
                .code("SUCCESS")
                .message("Cập nhật trạng thái thành công")
                .data(result)
                .build();
    }
}