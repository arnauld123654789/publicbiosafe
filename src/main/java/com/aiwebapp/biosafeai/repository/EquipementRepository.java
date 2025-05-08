package com.aiwebapp.biosafeai.repository;

import com.aiwebapp.biosafeai.entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    List<Equipement> findByProprietaireId(Long proprietaireId);
    List<Equipement> findByAvailable(boolean available);
} 