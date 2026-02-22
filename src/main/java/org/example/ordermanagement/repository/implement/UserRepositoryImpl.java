package org.example.ordermanagement.repository.implement;

import org.example.ordermanagement.model.domain.User;
import org.example.ordermanagement.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> storage = new LinkedHashMap<>();
    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }
    @Override
    public boolean existsByUsername(String username) {
        for (User user : storage.values()) {
            if (user.getUsername() != null && user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
}