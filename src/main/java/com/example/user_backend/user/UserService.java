package com.example.user_backend.user;

import org.springframework.http.ResponseEntity;

import com.example.user_backend.request.RegisterRequest;
import com.example.user_backend.request.UpdateUserRequest;

public interface UserService {
	
	ResponseEntity<?> register(RegisterRequest dto);
    ResponseEntity<?> login(String email, String password);
	ResponseEntity<?> getAllUsers();
	ResponseEntity<?> getById(Integer userId);
	ResponseEntity<?> updateUser(Integer id, UpdateUserRequest dto);
	ResponseEntity<?> deleteById(Integer id);
    
}
