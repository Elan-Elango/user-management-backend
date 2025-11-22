package com.example.user_backend.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.user_backend.request.ApiResponse;
import com.example.user_backend.request.RegisterRequest;
import com.example.user_backend.request.UpdateUserRequest;
import com.example.user_backend.request.UserResponse;
import com.example.user_backend.security.JwtService;
import com.example.user_backend.util.BaseService;
import com.example.user_backend.util.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final EmailService emailService;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public ResponseEntity<ApiResponse> register(RegisterRequest dto) {
		if (userRepository.existsByEmail(dto.getEmail()))
			return badRequest("Email already in use");

		var user = UserEntity.builder().name(dto.getName()).email(dto.getEmail())
				.password(encoder.encode(dto.getPassword())).createdAt(LocalDateTime.now()).build();
		
		UserEntity saved = userRepository.save(user);
        emailService.sendWelcomeEmail(saved.getEmail(), saved.getName());
		return created("Registration successful", user);
	}

	@Override
	public ResponseEntity<ApiResponse> login(String email, String password) {
		var user = userRepository.findByEmail(email).orElse(null);

		if (user == null)
			return notFound("User not found");
		if (!encoder.matches(password, user.getPassword()))
			return unauthorized("Invalid email or password");

		var token = jwtService.generateToken(user.getEmail());
		var data = java.util.Map.of("userId", user.getId(), "token", token);

		return ok("Login successful", data);
	}

	@Override
	public ResponseEntity<ApiResponse> getAllUsers() {
		try {
			List<UserResponse> users = userRepository.findAll().stream().map(this::mapToResponse).toList();

			log.info("Fetched {} users", users.size());
			return ok("Users fetched successfully", users);

		} catch (Exception ex) {
			log.error("Error fetching users", ex);
			return serverError("Unable to fetch users");
		}
	}

	private UserResponse mapToResponse(UserEntity user) {
		return UserResponse.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.createdAt(user.getCreatedAt()).build();
	}

	@Override
	public ResponseEntity<?> getById(Integer userId) {
		try {
			Optional<UserResponse> user = userRepository.findById(userId).map(this::mapToResponse);

			log.info("Fetched {} user detail by id", user);
			return ok("User detail fetched successfully by id", user);

		} catch (Exception ex) {
			log.error("Error fetching user detail by id", ex);
			return serverError("Unable to fetch user detail by id");
		}
	}
	
	@Override
	public ResponseEntity<?> deleteById(Integer userId) {
	    try {
	        var userOptional = userRepository.findById(userId);

	        if (userOptional.isEmpty()) {
	            return notFound("User not found with id: " + userId);
	        }

	        userRepository.deleteById(userId);

	        log.info("Deleted user successfully with id {}", userId);
	        return ok("User deleted successfully", null);

	    } catch (Exception ex) {
	        log.error("Error deleting user with id {}", userId, ex);
	        return serverError("Unable to delete user");
	    }
	}

	@Override
	public ResponseEntity<?> updateUser(Integer userId, UpdateUserRequest dto) {
	    try {
	        var userOptional = userRepository.findById(userId);

	        if (userOptional.isEmpty()) {
	            return notFound("User not found with id: " + userId);
	        }

	        var user = userOptional.get();
	        // Only update fields if provided
	        if (dto.getName() != null && !dto.getName().isEmpty()) {
	            user.setName(dto.getName());
	        }
	        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
	            // Optional: check for email uniqueness
	            if (userRepository.existsByEmail(dto.getEmail()) && !user.getEmail().equals(dto.getEmail())) {
	                return badRequest("Email already in use");
	            }
	            user.setEmail(dto.getEmail());
	        }

	        userRepository.save(user);

	        log.info("Updated user successfully with id {}", userId);
	        return ok("User updated successfully", mapToResponse(user));

	    } catch (Exception ex) {
	        log.error("Error updating user with id {}", userId, ex);
	        return serverError("Unable to update user");
	    }
	}

}
