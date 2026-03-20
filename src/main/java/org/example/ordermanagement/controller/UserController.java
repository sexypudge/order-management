package org.example.ordermanagement.controller;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.model.dto.request.RoleRequest;
import org.example.ordermanagement.model.dto.request.UserRequest;
import org.example.ordermanagement.model.dto.request.UserSearchRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.PageResponse;
import org.example.ordermanagement.model.dto.response.UserResponse;
import org.example.ordermanagement.service.UserService;
import org.example.ordermanagement.service.implement.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody UserRequest userRequest) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Successfully created user!")
                .result(userService.createUser(userRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Successfully get information of user!")
                .result(userService.getUserById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/admin/{userId}/assign-role")
    public ResponseEntity<ApiResponse<UserResponse>> assignRoles(
            @PathVariable Long userId,
            @RequestBody Set<String> roleNames
    ) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Successfully assigned roles to user!")
                .result(userService.assignRolesToUser(userId, roleNames))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestBody(required = false) @Valid UserSearchRequest userSearchRequest
    ) {
        UserSearchRequest searchRequest;
        if (userSearchRequest == null) {
            searchRequest = new UserSearchRequest();
        } else {
            searchRequest = userSearchRequest;
        }
        ApiResponse<PageResponse<UserResponse>> response = ApiResponse.<PageResponse<UserResponse>>builder()
                .code(1000)
                .message("Successfully listed users!")
                .result(userService.searchUsers(page, size, sortBy, sortDirection, searchRequest))
                .build();
        return ResponseEntity.ok(response);
    }

}
