package org.example.ordermanagement.model.domain;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.*;
import org.example.ordermanagement.common.enums.UserRole;

@Entity
@Builder
@Table(name = "role")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private UserRole name;
}
