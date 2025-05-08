package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.ArticleDto;
import com.aiwebapp.biosafeai.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public List<ArticleDto> getAll() {
        return articleService.getAll();
    }

    @GetMapping("/culture/{cultureId}")
    public List<ArticleDto> getByCultureId(@PathVariable Long cultureId) {
        return articleService.getByCultureId(cultureId);
    }

    @GetMapping("/publisher/{publisherId}")
    public List<ArticleDto> getByPublisherId(@PathVariable Long publisherId) {
        return articleService.getByPublisherId(publisherId);
    }

    @GetMapping("/type/{type}")
    public List<ArticleDto> getByType(@PathVariable String type) {
        return articleService.getByType(type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getById(@PathVariable Long id) {
        return articleService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ArticleDto create(@RequestBody ArticleDto dto) {
        return articleService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<ArticleDto> update(@PathVariable Long id, @RequestBody ArticleDto dto) {
        return articleService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGRONOME')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 