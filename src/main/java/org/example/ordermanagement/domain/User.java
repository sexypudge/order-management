package org.example.ordermanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.ordermanagement.common.enums.UserStatus;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Tự động tạo mã chuỗi không trùng lặp
    private String id;

    private String username;
    private String password;
    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}