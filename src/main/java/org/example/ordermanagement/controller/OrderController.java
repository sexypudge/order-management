package org.example.ordermanagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.example.ordermanagement.model.dto.request.*;
import org.example.ordermanagement.model.dto.response.*;
import org.example.ordermanagement.service.OrderService;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success(orderService.createOrder(request)));
    }
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getOrder() {
        return ResponseEntity.ok(ResponseUtil.success(orderService.getOrders()));
    }
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtil.success(orderService.getOrderById(id)));
    }
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderSearchResponse>>> searchOrders(
            @Valid @RequestBody OrderSearchRequest request,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be >= 0")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be >= 1")
            int size,
            @RequestParam(defaultValue = "orderCode")
            @Pattern(regexp = "orderCode|username|status",
                    message = "sortBy must be orderCode, username, or status"
            )
            String sortBy,
            @RequestParam(defaultValue = "ASC")
            @Pattern(regexp = "ASC|DESC",
                    message = "sortDirection must be ASC or DESC"
            )
            String sortDirection
    ) {
        return ResponseEntity.ok(
                ResponseUtil.success(orderService.searchOrders(request, page, size, sortBy, sortDirection))
        );
    }
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PatchMapping ("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> assignOrder(@PathVariable Long id,
                                                                @Valid @RequestBody AssignOrderRequest request) {
        return ResponseEntity.ok(ResponseUtil.success(orderService.assignStatusToOrder(id, request.getStatus()))
        );
    }
}