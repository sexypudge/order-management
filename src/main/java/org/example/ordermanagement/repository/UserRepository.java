package org.example.ordermanagement.repository;

import org.example.ordermanagement.model.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameIgnoreCase(String username);
    @Query("""
       SELECT u
       FROM User u
       WHERE (:id IS NULL OR u.id = :id)
     AND (:name IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')))
       """)
    Page<User> searchUsers(@Param("id") Long id,
                           @Param("name") String name,
                           Pageable pageable);
}