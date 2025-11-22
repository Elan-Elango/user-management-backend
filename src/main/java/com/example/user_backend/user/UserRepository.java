package com.example.user_backend.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<UserEntity, Integer> {

    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    
}