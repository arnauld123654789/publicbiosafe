package com.aiwebapp.biosafeai.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agriculteur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agriculteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String adresse;
    private String tel;
    private String mdp;
    private String sexe;

    // Optionally link to Utilisateur for authentication/authorization
    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
} 