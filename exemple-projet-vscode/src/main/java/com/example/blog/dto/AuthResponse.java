package com.example.blog.dto;

import lombok.Data;
import lombok.Builder;
import com.example.blog.enums.UserRole;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private UserRole role;
    private String message;
}