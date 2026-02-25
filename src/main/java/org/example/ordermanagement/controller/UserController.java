package org.example.ordermanagement.controller;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody UserRequest userRequest) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Successfully created user!")
                .result(userService.createUser(userRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("Get information all users")
                .result(userService.getAllUsers())
                .build();
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
    }
}
