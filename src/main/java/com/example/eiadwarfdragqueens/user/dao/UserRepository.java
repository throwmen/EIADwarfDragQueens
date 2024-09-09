package com.example.eiadwarfdragqueens.user.dao;

import com.example.eiadwarfdragqueens.user.modelEntity.User;
import com.example.eiadwarfdragqueens.user.modelEntity.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    Optional<User> findByEmail(String email);
}
