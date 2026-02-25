package org.example.ordermanagement.model.domain;

import jakarta.persistence.*;
import org.example.ordermanagement.common.enums.UserRole;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private UserRole name;

    public Role() {}

    public Role(UserRole name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public UserRole getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(UserRole name) {
        this.name = name;
    }
}