package org.example.ordermanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class UserRequest {
    private String username;
    private String password;
    Set<Integer> roleIds;
}