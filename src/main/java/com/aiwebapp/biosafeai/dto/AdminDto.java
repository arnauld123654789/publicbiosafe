package com.aiwebapp.biosafeai.dto;

import lombok.Data;

@Data
public class AdminDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long utilisateurId;
} 