package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.ProprietaireEquipementDto;
import com.aiwebapp.biosafeai.service.ProprietaireEquipementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proprietaires")
@RequiredArgsConstructor
public class ProprietaireEquipementController {
    private final ProprietaireEquipementService proprietaireEquipementService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public List<ProprietaireEquipementDto> getAll() {
        return proprietaireEquipementService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public ResponseEntity<ProprietaireEquipementDto> getById(@PathVariable Long id) {
        return proprietaireEquipementService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public ProprietaireEquipementDto create(@RequestBody ProprietaireEquipementDto dto) {
        return proprietaireEquipementService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public ResponseEntity<ProprietaireEquipementDto> update(@PathVariable Long id, @RequestBody ProprietaireEquipementDto dto) {
        return proprietaireEquipementService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPRIETAIRE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        proprietaireEquipementService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 