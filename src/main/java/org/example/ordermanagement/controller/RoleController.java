package org.example.ordermanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.ordermanagement.model.dto.request.RoleRequest;
import org.example.ordermanagement.model.dto.response.ApiResponse;
import org.example.ordermanagement.model.dto.response.RoleResponse;
import org.example.ordermanagement.service.implement.RoleServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private RoleServiceImpl roleService;
    @PostMapping("/admin/create-role")
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .code(1000)
                .message("Successfully created role!")
                .result(roleService.createRole(roleRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
