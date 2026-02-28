package org.example.ordermanagement.controller;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


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