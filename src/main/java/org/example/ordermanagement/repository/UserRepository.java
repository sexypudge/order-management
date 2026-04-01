package org.example.ordermanagement.repository;

import org.example.ordermanagement.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

//    LEFT JOIN
//    Lấy tất cả User
//    Nếu có roles thì join
//    Nếu không có roles  vẫn lấy User (roles = null)
@Query("SELECT DISTINCT u FROM User u " +
        "LEFT JOIN u.roles r " +
        "WHERE (:id IS NULL OR u.id = :id) " +
        "AND (:name IS NULL OR u.username LIKE CONCAT('%', :name, '%')) " +
        "AND (:roleId IS NULL OR r.id = :roleId)")
Page<User> searchUsersBasic(@Param("id") Long id,
                            @Param("name") String name,
                            @Param("roleId") Integer roleId,
                            Pageable pageable);

    Optional<User> findByUsername(String username);
}