    package org.example.ordermanagement.controller;

    import lombok.AccessLevel;
    import lombok.RequiredArgsConstructor;
    import lombok.experimental.FieldDefaults;
    import org.example.ordermanagement.model.dto.response.ApiResponse;
    import org.example.ordermanagement.model.domain.Role;
    import org.example.ordermanagement.service.RoleService; // Giả định bạn có RoleService
    import org.example.ordermanagement.service.UserService;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Set;

    @RestController
    @RequestMapping("/api/role/admin") // Khớp với yêu cầu: /api/role/admin/
    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public class RoleController {

        RoleService roleService;
        UserService userService;

        // API: Tạo Role mới (Yêu cầu bước 1)
        // URL: POST /api/role/admin/
        @PostMapping("/")
        public ApiResponse<Role> createRole(@RequestBody Role roleRequest) {
            var result = roleService.createRole(roleRequest);
            return ApiResponse.<Role>builder()
                    .message("Tạo Role thành công")
                    .data(result)
                    .build();
        }

        // API: Gán Role cho User (Yêu cầu bước 1)
        // URL: PUT /api/role/admin/assign-role
        @PutMapping("/assign-role")
        public ApiResponse<Object> assignRole(
                @RequestParam String userId,
                @RequestBody Set<Long> roleIds) {

            var result = userService.assignRoles(userId, roleIds);

            return ApiResponse.builder()
                    .message("Gán quyền cho người dùng thành công")
                    .data(result)
                    .build();
        }

        // API: Lấy danh sách tất cả Role
        @GetMapping("/all")
        public ApiResponse<List<Role>> getAllRoles() {
            return ApiResponse.<List<Role>>builder()
                    .data(roleService.getAllRoles())
                    .build();
        }
    }