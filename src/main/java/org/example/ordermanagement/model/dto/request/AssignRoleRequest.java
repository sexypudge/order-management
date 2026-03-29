package org.example.ordermanagement.model.dto.request;

import lombok.Data;

import java.util.Set;
@Data

public class AssignRoleRequest {
    private String userId;   // Khớp với JSON {"userId": "9"}
    private Set<Long> roleIds; // Khớp với JSON {"roleIds": [3]}
}