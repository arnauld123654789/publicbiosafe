package com.aiwebapp.biosafeai.dto;

import lombok.Data;

@Data
public class AgriculteurDto {
    private Long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String tel;
    private String sexe;
    private Long utilisateurId;
} 