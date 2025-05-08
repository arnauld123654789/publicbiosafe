package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.ProprietaireEquipementDto;
import com.aiwebapp.biosafeai.entity.ProprietaireEquipement;
import com.aiwebapp.biosafeai.entity.Utilisateur;
import com.aiwebapp.biosafeai.repository.ProprietaireEquipementRepository;
import com.aiwebapp.biosafeai.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProprietaireEquipementService {
    private final ProprietaireEquipementRepository proprietaireEquipementRepository;
    private final UtilisateurRepository utilisateurRepository;

    public List<ProprietaireEquipementDto> getAll() {
        return proprietaireEquipementRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ProprietaireEquipementDto> getById(Long id) {
        return proprietaireEquipementRepository.findById(id)
                .map(this::toDto);
    }

    public ProprietaireEquipementDto create(ProprietaireEquipementDto dto) {
        ProprietaireEquipement proprietaire = toEntity(dto);
        if (dto.getUtilisateurId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur not found"));
            proprietaire.setUtilisateur(utilisateur);
        }
        return toDto(proprietaireEquipementRepository.save(proprietaire));
    }

    public Optional<ProprietaireEquipementDto> update(Long id, ProprietaireEquipementDto dto) {
        return proprietaireEquipementRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(dto.getFirstName());
                    existing.setLastName(dto.getLastName());
                    existing.setEmail(dto.getEmail());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    existing.setAddress(dto.getAddress());
                    existing.setPhoto(dto.getPhoto());
                    if (dto.getUtilisateurId() != null) {
                        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                                .orElseThrow(() -> new RuntimeException("Utilisateur not found"));
                        existing.setUtilisateur(utilisateur);
                    }
                    return toDto(proprietaireEquipementRepository.save(existing));
                });
    }

    public void delete(Long id) {
        proprietaireEquipementRepository.deleteById(id);
    }

    private ProprietaireEquipementDto toDto(ProprietaireEquipement p) {
        ProprietaireEquipementDto dto = new ProprietaireEquipementDto();
        dto.setId(p.getId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setEmail(p.getEmail());
        dto.setPhoneNumber(p.getPhoneNumber());
        dto.setAddress(p.getAddress());
        dto.setPhoto(p.getPhoto());
        dto.setUtilisateurId(p.getUtilisateur() != null ? p.getUtilisateur().getId() : null);
        return dto;
    }

    private ProprietaireEquipement toEntity(ProprietaireEquipementDto dto) {
        ProprietaireEquipement p = new ProprietaireEquipement();
        p.setId(dto.getId());
        p.setFirstName(dto.getFirstName());
        p.setLastName(dto.getLastName());
        p.setEmail(dto.getEmail());
        p.setPhoneNumber(dto.getPhoneNumber());
        p.setAddress(dto.getAddress());
        p.setPhoto(dto.getPhoto());
        return p;
    }
} 