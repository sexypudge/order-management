package org.example.ordermanagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PreAuthorize("hasAnyRole('ADMIN','STAFF','CUSTOMER')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF') or authentication.principal.id ==#id")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Successfully get information of user!")
                .result(userService.getUserById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> search(
            @Valid @RequestBody UserSearchRequest userSearchRequest,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be >= 0")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 0, message = "Size must be >= 0")
            int size,
            @RequestParam(defaultValue = "id")
            @Pattern(regexp = "id|name",
                    message = "sortBy must be id or name")
            String sortBy,
            @RequestParam(defaultValue = "ASC")
            @Pattern(regexp = "ASC|DESC",
                    message = "sortDirection must be ASC or DESC")
            String sortDirection
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
