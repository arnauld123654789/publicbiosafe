package com.aiwebapp.biosafeai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "equipement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @ElementCollection
    @CollectionTable(name = "equipement_media", joinColumns = @JoinColumn(name = "equipement_id"))
    @Column(name = "media_url")
    private List<String> mediaUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private ProprietaireEquipement proprietaire;

    @Column(name = "rent_price", nullable = false)
    private BigDecimal rentPrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private boolean available;
} 