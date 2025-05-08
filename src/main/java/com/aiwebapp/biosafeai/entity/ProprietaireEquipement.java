package com.aiwebapp.biosafeai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "proprietaire_equipement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProprietaireEquipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column
    private String address;

    @Column
    private String photo;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL)
    private List<Equipement> equipements;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
} 