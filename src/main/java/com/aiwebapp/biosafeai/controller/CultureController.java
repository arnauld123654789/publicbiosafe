package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.CultureDto;
import com.aiwebapp.biosafeai.service.CultureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cultures")
@RequiredArgsConstructor
public class CultureController {
    private final CultureService cultureService;

    @GetMapping
    public ResponseEntity<List<CultureDto>> getAll() {
        return ResponseEntity.ok(cultureService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CultureDto> getById(@PathVariable Long id) {
        return cultureService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CultureDto>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(cultureService.getByType(type));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<CultureDto> create(@RequestBody CultureDto cultureDto) {
        try {
            return ResponseEntity.ok(cultureService.createCulture(cultureDto, null));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/media")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<CultureDto> addMedia(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            CultureDto cultureDto = cultureService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Culture not found"));
            return ResponseEntity.ok(cultureService.createCulture(cultureDto, files));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<CultureDto> update(@PathVariable Long id, @RequestBody CultureDto cultureDto) {
        return cultureService.update(id, cultureDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            cultureService.deleteCulture(id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 