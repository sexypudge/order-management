package org.example.ordermanagement.repository;

import org.example.ordermanagement.common.enums.UserRole;
import org.example.ordermanagement.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.roles r " +
            "WHERE (:id IS NULL OR u.id = :id) " +
            "AND (:name IS NULL OR u.username LIKE %:name%) " +
            "AND (:role IS NULL OR r.name = :role)")
    Page<User> searchUsersBasic(@Param("id") Long id,
                                @Param("name") String name,
                                @Param("role")UserRole role,
                                Pageable pageable);
}
