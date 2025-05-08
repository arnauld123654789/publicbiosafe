package com.aiwebapp.biosafeai.dto;

import lombok.Data;

@Data
public class AgronomeDto {
    private Long id;
    private String nom;
    private String prenom;
    private String sexe;
    private String specialite;
    private String photo;
    private String tel;
    private String mail;
    private String adresse;
    private Long utilisateurId;
} 