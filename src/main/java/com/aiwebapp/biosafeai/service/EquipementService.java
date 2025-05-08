package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.EquipementDto;
import com.aiwebapp.biosafeai.entity.Equipement;
import com.aiwebapp.biosafeai.entity.ProprietaireEquipement;
import com.aiwebapp.biosafeai.repository.EquipementRepository;
import com.aiwebapp.biosafeai.repository.ProprietaireEquipementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipementService {
    private final EquipementRepository equipementRepository;
    private final ProprietaireEquipementRepository proprietaireRepository;

    public List<EquipementDto> getAll() {
        return equipementRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<EquipementDto> getByProprietaireId(Long proprietaireId) {
        return equipementRepository.findByProprietaireId(proprietaireId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<EquipementDto> getAvailable() {
        return equipementRepository.findByAvailable(true).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<EquipementDto> getById(Long id) {
        return equipementRepository.findById(id)
                .map(this::toDto);
    }

    public EquipementDto create(EquipementDto dto) {
        Equipement equipement = toEntity(dto);
        ProprietaireEquipement proprietaire = proprietaireRepository.findById(dto.getProprietaireId())
                .orElseThrow(() -> new RuntimeException("Proprietaire not found"));
        equipement.setProprietaire(proprietaire);
        return toDto(equipementRepository.save(equipement));
    }

    public Optional<EquipementDto> update(Long id, EquipementDto dto) {
        return equipementRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setType(dto.getType());
                    existing.setMediaUrls(dto.getMediaUrls());
                    existing.setRentPrice(dto.getRentPrice());
                    existing.setSellingPrice(dto.getSellingPrice());
                    existing.setAvailable(dto.isAvailable());
                    if (dto.getProprietaireId() != null) {
                        ProprietaireEquipement proprietaire = proprietaireRepository.findById(dto.getProprietaireId())
                                .orElseThrow(() -> new RuntimeException("Proprietaire not found"));
                        existing.setProprietaire(proprietaire);
                    }
                    return toDto(equipementRepository.save(existing));
                });
    }

    public void delete(Long id) {
        equipementRepository.deleteById(id);
    }

    private EquipementDto toDto(Equipement e) {
        EquipementDto dto = new EquipementDto();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setType(e.getType());
        dto.setMediaUrls(e.getMediaUrls());
        dto.setProprietaireId(e.getProprietaire().getId());
        dto.setRentPrice(e.getRentPrice());
        dto.setSellingPrice(e.getSellingPrice());
        dto.setAvailable(e.isAvailable());
        return dto;
    }

    private Equipement toEntity(EquipementDto dto) {
        Equipement e = new Equipement();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setType(dto.getType());
        e.setMediaUrls(dto.getMediaUrls());
        e.setRentPrice(dto.getRentPrice());
        e.setSellingPrice(dto.getSellingPrice());
        e.setAvailable(dto.isAvailable());
        return e;
    }
} 