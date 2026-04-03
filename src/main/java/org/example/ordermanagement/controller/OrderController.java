package org.example.ordermanagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.aspectj.weaver.ast.Or;
import org.example.ordermanagement.common.enums.OrderStatus;
import org.example.ordermanagement.model.domain.Order;
import org.example.ordermanagement.model.dto.request.*;
import org.example.ordermanagement.model.dto.response.*;
import org.example.ordermanagement.service.OrderService;
import org.example.ordermanagement.service.implement.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody OrderCreateRequest orderCreateRequest) {
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Successfully created order!")
                .result(orderService.createOrder(orderCreateRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id,
                                                               @AuthenticationPrincipal String username ){
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Successfully get order!")
                .result(orderService.getOrderById(id, username))
                .build();
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/update-order/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(@PathVariable Long id,
                                                                  @RequestBody UpdateOrderStatusRequest request) {
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .code(1000)
                .message("Order updated!")
                .result(orderService.updateOrderStatus(id, request))
                .build();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CUSTOMER')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> search(
            @Valid @RequestBody OrderSearchRequest orderSearchRequest,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be >= 0")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be >= 1")
            int size,
            @RequestParam(defaultValue = "orderCode")
            @Pattern(regexp = "orderCode|name|status|id",
                    message = "sortBy must be orderCode, username, or status"
            )
            String sortBy,
            @RequestParam(defaultValue = "ASC")
            @Pattern(regexp = "ASC|DESC",
                    message = "sortDirection must be ASC or DESC"
            )
            String sortDirection
    ){
        OrderSearchRequest searchRequest;
        if(orderSearchRequest==null){
            searchRequest = new OrderSearchRequest();
        }else {
            searchRequest = orderSearchRequest;
        }
        ApiResponse<PageResponse<OrderResponse>> response= ApiResponse.<PageResponse<OrderResponse>>builder()
                .code(1000)
                .message("Successfully searched  orders!")
                .result(orderService.searchOrders(page,size,sortBy,sortDirection,searchRequest))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<OrderHistoryResponse>>> getOrderHistory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestBody(required = false) @Valid OrderHistoryRequest orderHistoryRequest
    ) {
        OrderHistoryRequest searchRequest = (orderHistoryRequest == null)
                ? new OrderHistoryRequest()
                : orderHistoryRequest;

        ApiResponse<PageResponse<OrderHistoryResponse>> response = ApiResponse.<PageResponse<OrderHistoryResponse>>builder()
                .code(1000)
                .message("Successfully retrieved order history!")
                .result(orderService.getOrderHistory(id, page, size, sortBy, sortDirection, searchRequest))
                .build();

        return ResponseEntity.ok(response);
    }
}
