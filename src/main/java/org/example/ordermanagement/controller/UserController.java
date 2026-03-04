package org.example.ordermanagement.controller;

import org.example.ordermanagement.model.dto.request.AssignRoleRequest;
import org.example.ordermanagement.model.dto.request.CreateUserRequest;
import org.example.ordermanagement.model.dto.request.UserSearchRequest;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.RoleService;
import org.example.ordermanagement.service.UserService;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getUsers() {
        return ResponseEntity.ok(ResponseUtil.success(userService.getUsers()));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success(userService.createUser(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ResponseUtil.success(user));
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestBody UserSearchRequest request,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(
                ResponseUtil.success(userService.searchUsers(request, page, size, sortBy, sortDirection))
        );
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Map<String, Object>> assignRole(@PathVariable Long id,
                                                          @RequestBody AssignRoleRequest request) {
        return ResponseEntity.ok(ResponseUtil.success(roleService.assignRoleToUser(id, request.getRole())));
    }
}