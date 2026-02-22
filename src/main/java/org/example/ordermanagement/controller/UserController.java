package org.example.ordermanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.dto.request.UserRequest;
import org.example.ordermanagement.dto.response.ApiResponse;
import org.example.ordermanagement.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là nơi nhận API
@RequestMapping("/users") // Đường dẫn chính là /users
@RequiredArgsConstructor // Tự động kết nối với Service (Dependency Injection)
public class UserController {

    // Gọi tên Interface, không gọi class Impl (Đây là quy tắc DI)
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getList() {
        return ApiResponse.<List<UserResponse>>builder()
                .code("SUCCESS")
                .data(userService.getAllUsers())
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code("CREATED")
                .message("Tạo thành công!")
                .data(userService.createUser(request))
                .build();
    }
}