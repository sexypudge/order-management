package org.example.ordermanagement.repository;

import org.example.ordermanagement.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>findByUsername(String username);
}
