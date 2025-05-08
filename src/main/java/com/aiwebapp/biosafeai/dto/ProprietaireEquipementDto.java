package com.aiwebapp.biosafeai.dto;

import lombok.Data;

@Data
public class ProprietaireEquipementDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String photo;
    private Long utilisateurId;
} 