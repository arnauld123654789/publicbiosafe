package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.AgronomeDto;
import com.aiwebapp.biosafeai.service.AgronomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agronomes")
@RequiredArgsConstructor
public class AgronomeController {
    private final AgronomeService agronomeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public List<AgronomeDto> getAll() {
        return agronomeService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<AgronomeDto> getById(@PathVariable Long id) {
        return agronomeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public AgronomeDto create(@RequestBody AgronomeDto dto) {
        return agronomeService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<AgronomeDto> update(@PathVariable Long id, @RequestBody AgronomeDto dto) {
        return agronomeService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agronomeService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 