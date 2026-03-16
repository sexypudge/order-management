package org.example.ordermanagement.model.dto.request;

import java.util.*;

import lombok.*;

@Getter @Setter
public class UserRequest {
    private String id;
    private String username;
    private String password;
    Set<Integer> roleIds;
}