package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.AgriculteurDto;
import com.aiwebapp.biosafeai.entity.Agriculteur;
import com.aiwebapp.biosafeai.entity.Utilisateur;
import com.aiwebapp.biosafeai.repository.AgriculteurRepository;
import com.aiwebapp.biosafeai.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgriculteurService {
    private final AgriculteurRepository agriculteurRepository;
    private final UtilisateurRepository utilisateurRepository;

    public List<AgriculteurDto> getAll() {
        return agriculteurRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<AgriculteurDto> getById(Long id) {
        return agriculteurRepository.findById(id).map(this::toDto);
    }

    public AgriculteurDto create(AgriculteurDto dto) {
        Agriculteur agriculteur = toEntity(dto);
        if (dto.getUtilisateurId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId()).orElse(null);
            agriculteur.setUtilisateur(utilisateur);
        }
        return toDto(agriculteurRepository.save(agriculteur));
    }

    public Optional<AgriculteurDto> update(Long id, AgriculteurDto dto) {
        return agriculteurRepository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setPrenom(dto.getPrenom());
            existing.setAdresse(dto.getAdresse());
            existing.setTel(dto.getTel());
            existing.setSexe(dto.getSexe());
            if (dto.getUtilisateurId() != null) {
                Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId()).orElse(null);
                existing.setUtilisateur(utilisateur);
            }
            return toDto(agriculteurRepository.save(existing));
        });
    }

    public void delete(Long id) {
        agriculteurRepository.deleteById(id);
    }

    private AgriculteurDto toDto(Agriculteur a) {
        AgriculteurDto dto = new AgriculteurDto();
        dto.setId(a.getId());
        dto.setNom(a.getNom());
        dto.setPrenom(a.getPrenom());
        dto.setAdresse(a.getAdresse());
        dto.setTel(a.getTel());
        dto.setSexe(a.getSexe());
        dto.setUtilisateurId(a.getUtilisateur() != null ? a.getUtilisateur().getId() : null);
        return dto;
    }

    private Agriculteur toEntity(AgriculteurDto dto) {
        Agriculteur a = new Agriculteur();
        a.setId(dto.getId());
        a.setNom(dto.getNom());
        a.setPrenom(dto.getPrenom());
        a.setAdresse(dto.getAdresse());
        a.setTel(dto.getTel());
        a.setSexe(dto.getSexe());
        return a;
    }
} 