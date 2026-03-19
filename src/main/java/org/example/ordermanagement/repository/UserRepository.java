package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.model.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameIgnoreCase(String username);
    @EntityGraph(attributePaths = {"roles"})
    @Query("""
        SELECT  u
        FROM User u
        LEFT JOIN u.roles r
        WHERE (:id IS NULL OR u.id = :id)
          AND (:name IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:role IS NULL OR r.name = :role)
    """)
    Page<User> searchUsers(@Param("id") Long id,
                           @Param("name") String name,
                           @Param("role") UserRole role,
                           Pageable pageable);
    Optional<User> findByUsernameIgnoreCase(String username);
}