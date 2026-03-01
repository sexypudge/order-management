package org.example.ordermanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.ordermanagement.common.enums.RoleName;

@Entity
@Table(name = "roles")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    RoleName name;
}