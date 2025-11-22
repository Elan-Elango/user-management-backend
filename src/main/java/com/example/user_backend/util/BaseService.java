package com.example.user_backend.util;

import org.springframework.http.ResponseEntity;
import com.example.user_backend.request.ApiResponse;

public abstract class BaseService {

    protected ResponseEntity<ApiResponse> ok(String msg, Object data) {
        return ResponseEntity.ok(ApiResponse.success(msg, data));
    }

    protected ResponseEntity<ApiResponse> created(String msg, Object data) {
        return ResponseEntity.status(201).body(ApiResponse.success(msg, data));
    }

    protected ResponseEntity<ApiResponse> badRequest(String msg) {
        return ResponseEntity.badRequest().body(ApiResponse.failure(msg));
    }

    protected ResponseEntity<ApiResponse> notFound(String msg) {
        return ResponseEntity.status(404).body(ApiResponse.failure(msg));
    }

    protected ResponseEntity<ApiResponse> unauthorized(String msg) {
        return ResponseEntity.status(401).body(ApiResponse.failure(msg));
    }

    protected ResponseEntity<ApiResponse> serverError(String msg) {
        return ResponseEntity.internalServerError().body(ApiResponse.failure(msg));
    }
}

