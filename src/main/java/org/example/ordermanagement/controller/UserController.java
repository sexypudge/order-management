package org.example.ordermanagement.controller;

import jakarta.validation.Valid;
import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.model.dto.request.UserSearchRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.model.dto.response.UserSearchResponse;
import org.example.ordermanagement.service.RoleService;
import org.example.ordermanagement.service.UserService;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getUsers() {
        return ResponseEntity.ok(ResponseUtil.success(userService.getUsers()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success(userService.createUser(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtil.success(userService.getUserById(id)));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<UserSearchResponse>>> searchUsers(
           @Valid @RequestBody UserSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity.ok(
                ResponseUtil.success(userService.searchUsers(request, page, size, sortBy, sortDirection))
        );
    }


}