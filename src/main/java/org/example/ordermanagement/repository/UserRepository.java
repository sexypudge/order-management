package org.example.ordermanagement.repository;

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
    Optional<User>findByUsername(String username);
    @Query("SELECT u FROM User u WHERE " +
            "(:id IS NULL OR u.id = :id) AND " +
            "(:name IS NULL OR u.username LIKE %:name%)")
    Page<User> searchUsersBasic(@Param("id") Long id,
                                @Param("name") String name,
                                Pageable pageable);
}
