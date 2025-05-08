package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.AdminDto;
import com.aiwebapp.biosafeai.entity.Admin;
import com.aiwebapp.biosafeai.entity.Utilisateur;
import com.aiwebapp.biosafeai.repository.AdminRepository;
import com.aiwebapp.biosafeai.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final UtilisateurRepository utilisateurRepository;

    public List<AdminDto> getAll() {
        return adminRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<AdminDto> getById(Long id) {
        return adminRepository.findById(id)
                .map(this::toDto);
    }

    public AdminDto create(AdminDto dto) {
        Admin admin = toEntity(dto);
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur not found"));
        admin.setUtilisateur(utilisateur);
        return toDto(adminRepository.save(admin));
    }

    public Optional<AdminDto> update(Long id, AdminDto dto) {
        return adminRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(dto.getFirstName());
                    existing.setLastName(dto.getLastName());
                    existing.setEmail(dto.getEmail());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    return toDto(adminRepository.save(existing));
                });
    }

    public void delete(Long id) {
        adminRepository.deleteById(id);
    }

    private AdminDto toDto(Admin a) {
        AdminDto dto = new AdminDto();
        dto.setId(a.getId());
        dto.setFirstName(a.getFirstName());
        dto.setLastName(a.getLastName());
        dto.setEmail(a.getEmail());
        dto.setPhoneNumber(a.getPhoneNumber());
        dto.setUtilisateurId(a.getUtilisateur().getId());
        return dto;
    }

    private Admin toEntity(AdminDto dto) {
        Admin a = new Admin();
        a.setId(dto.getId());
        a.setFirstName(dto.getFirstName());
        a.setLastName(dto.getLastName());
        a.setEmail(dto.getEmail());
        a.setPhoneNumber(dto.getPhoneNumber());
        return a;
    }
} 