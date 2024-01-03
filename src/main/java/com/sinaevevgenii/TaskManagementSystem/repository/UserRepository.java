package com.sinaevevgenii.TaskManagementSystem.repository;

import com.sinaevevgenii.TaskManagementSystem.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
