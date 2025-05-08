package com.aiwebapp.biosafeai.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agronome")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agronome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String sexe;
    private String specialite;
    private String photo;
    private String tel;
    private String mail;
    private String adresse;

    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
} 