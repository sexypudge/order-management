package org.example.ordermanagement.controller;

import org.example.ordermanagement.model.dto.request.CreateRoleRequest;
import org.example.ordermanagement.service.RoleService;
import org.example.ordermanagement.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRole(@RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(201)
                .body(ResponseUtil.success(roleService.createRole(request.getName())));
    }
}