package org.example.ordermanagement.repository;

import org.example.ordermanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // kểm tra xem username đã tồn tại chưa
    boolean existsByUsername(String username);
}