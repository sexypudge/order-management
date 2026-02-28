package org.example.ordermanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.ordermanagement.common.enums.RoleName;

@Entity
@Table(name = "roles")
@Getter @Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}