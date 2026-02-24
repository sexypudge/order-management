package org.example.ordermanagement.controller;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    ApiResponse<UserResponse> create(@RequestBody UserRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .ResCode(1000)
                .message("Successfully created user!")
                .result(userService.createUser(userRequest))
                .build();
    }

    @GetMapping
    ApiResponse <List<UserResponse>> getUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .ResCode(1000)
                .message("Get information all users")
                .result(userService.getAllUsers())
                .build();
    }
}
