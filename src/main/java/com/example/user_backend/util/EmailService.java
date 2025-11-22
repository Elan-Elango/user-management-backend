package com.example.user_backend.util;

public interface EmailService {
    void sendWelcomeEmail(String to, String name);
}