package org.example.ordermanagement.repository;

import org.example.ordermanagement.model.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);


    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE " +
            "(:id IS NULL OR u.id = :id) AND " +
            "(:name IS NULL OR u.username LIKE CONCAT('%', :name, '%'))")
    Page<User> searchUsersBasic(@Param("id") Long id,
                                @Param("name") String name,
                                Pageable pageable);
}