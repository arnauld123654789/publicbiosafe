package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.AgronomeDto;
import com.aiwebapp.biosafeai.entity.Agronome;
import com.aiwebapp.biosafeai.entity.Utilisateur;
import com.aiwebapp.biosafeai.repository.AgronomeRepository;
import com.aiwebapp.biosafeai.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgronomeService {
    private final AgronomeRepository agronomeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public List<AgronomeDto> getAll() {
        return agronomeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<AgronomeDto> getById(Long id) {
        return agronomeRepository.findById(id).map(this::toDto);
    }

    public AgronomeDto create(AgronomeDto dto) {
        Agronome agronome = toEntity(dto);
        if (dto.getUtilisateurId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId()).orElse(null);
            agronome.setUtilisateur(utilisateur);
        }
        return toDto(agronomeRepository.save(agronome));
    }

    public Optional<AgronomeDto> update(Long id, AgronomeDto dto) {
        return agronomeRepository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setPrenom(dto.getPrenom());
            existing.setSexe(dto.getSexe());
            existing.setSpecialite(dto.getSpecialite());
            existing.setPhoto(dto.getPhoto());
            existing.setTel(dto.getTel());
            existing.setMail(dto.getMail());
            existing.setAdresse(dto.getAdresse());
            if (dto.getUtilisateurId() != null) {
                Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId()).orElse(null);
                existing.setUtilisateur(utilisateur);
            }
            return toDto(agronomeRepository.save(existing));
        });
    }

    public void delete(Long id) {
        agronomeRepository.deleteById(id);
    }

    private AgronomeDto toDto(Agronome a) {
        AgronomeDto dto = new AgronomeDto();
        dto.setId(a.getId());
        dto.setNom(a.getNom());
        dto.setPrenom(a.getPrenom());
        dto.setSexe(a.getSexe());
        dto.setSpecialite(a.getSpecialite());
        dto.setPhoto(a.getPhoto());
        dto.setTel(a.getTel());
        dto.setMail(a.getMail());
        dto.setAdresse(a.getAdresse());
        dto.setUtilisateurId(a.getUtilisateur() != null ? a.getUtilisateur().getId() : null);
        return dto;
    }

    private Agronome toEntity(AgronomeDto dto) {
        Agronome a = new Agronome();
        a.setId(dto.getId());
        a.setNom(dto.getNom());
        a.setPrenom(dto.getPrenom());
        a.setSexe(dto.getSexe());
        a.setSpecialite(dto.getSpecialite());
        a.setPhoto(dto.getPhoto());
        a.setTel(dto.getTel());
        a.setMail(dto.getMail());
        a.setAdresse(dto.getAdresse());
        return a;
    }
} 