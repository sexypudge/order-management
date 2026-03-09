package org.example.ordermanagement.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.request.UserSearchRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        UserResponse result = userService.createUser(request);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("SUCCESS")
                .message("Tạo người dùng thành công")
                .data(result)
                .build();


        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> result = userService.getAllUsers();

        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .data(result)
                .build();


        return ResponseEntity.ok(apiResponse);
    }


    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String userId) {
        UserResponse result = userService.getUserById(userId);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .data(result)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/search")
    public ApiResponse<PageResponse<UserResponse>> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestBody(required = false) UserSearchRequest request) {

        var result = userService.searchUsers(page, size, sortBy, sortDirection, request);

        return ApiResponse.<PageResponse<UserResponse>>builder()
                .data(result)
                .build();
    }
    @PutMapping("/{userId}/roles")
    public ApiResponse<UserResponse> assignRoles(
            @PathVariable Long userId,
            @RequestBody Set<Integer> roleIds) {

        var result = userService.assignRoles(userId, roleIds);

        return ApiResponse.<UserResponse>builder()
                .message("Cập nhật quyền thành công")
                .data(result)
                .build();
    }
}