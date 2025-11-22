package com.example.user_backend.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
}

