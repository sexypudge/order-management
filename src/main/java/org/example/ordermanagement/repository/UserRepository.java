package org.example.ordermanagement.repository;

import org.example.ordermanagement.model.domain.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User save(User user);
    boolean existsByUsername(String username);
}