package com.aiwebapp.biosafeai.dto;

import com.aiwebapp.biosafeai.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
} 