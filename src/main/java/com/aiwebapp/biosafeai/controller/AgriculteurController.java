package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.AgriculteurDto;
import com.aiwebapp.biosafeai.service.AgriculteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agriculteurs")
@RequiredArgsConstructor
public class AgriculteurController {
    private final AgriculteurService agriculteurService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRICULTEUR')")
    public List<AgriculteurDto> getAll() {
        return agriculteurService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRICULTEUR')")
    public ResponseEntity<AgriculteurDto> getById(@PathVariable Long id) {
        return agriculteurService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRICULTEUR')")
    public AgriculteurDto create(@RequestBody AgriculteurDto dto) {
        return agriculteurService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRICULTEUR')")
    public ResponseEntity<AgriculteurDto> update(@PathVariable Long id, @RequestBody AgriculteurDto dto) {
        return agriculteurService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRICULTEUR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agriculteurService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 