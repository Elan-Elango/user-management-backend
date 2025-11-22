package com.example.user_backend.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.user_backend.request.LoginRequest;
import com.example.user_backend.request.RegisterRequest;
import com.example.user_backend.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest dto) {
        return userService.register(dto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        return userService.login(dto.getEmail(), dto.getPassword());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UpdateUserRequest dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return userService.deleteById(id);
    }
}

