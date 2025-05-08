package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.EquipementDto;
import com.aiwebapp.biosafeai.service.EquipementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementController {
    private final EquipementService equipementService;

    @GetMapping
    public List<EquipementDto> getAll() {
        return equipementService.getAll();
    }

    @GetMapping("/available")
    public List<EquipementDto> getAvailable() {
        return equipementService.getAvailable();
    }

    @GetMapping("/proprietaire/{proprietaireId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public List<EquipementDto> getByProprietaireId(@PathVariable Long proprietaireId) {
        return equipementService.getByProprietaireId(proprietaireId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipementDto> getById(@PathVariable Long id) {
        return equipementService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public EquipementDto create(@RequestBody EquipementDto dto) {
        return equipementService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public ResponseEntity<EquipementDto> update(@PathVariable Long id, @RequestBody EquipementDto dto) {
        return equipementService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipementService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 