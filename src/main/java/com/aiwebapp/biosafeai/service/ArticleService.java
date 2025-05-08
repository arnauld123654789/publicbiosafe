package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.ArticleDto;
import com.aiwebapp.biosafeai.entity.Article;
import com.aiwebapp.biosafeai.entity.Admin;
import com.aiwebapp.biosafeai.entity.Culture;
import com.aiwebapp.biosafeai.repository.ArticleRepository;
import com.aiwebapp.biosafeai.repository.AdminRepository;
import com.aiwebapp.biosafeai.repository.CultureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CultureRepository cultureRepository;
    private final AdminRepository adminRepository;
    private final FileStorageService fileStorageService;

    public List<ArticleDto> getAll() {
        return articleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ArticleDto> getByCultureId(Long cultureId) {
        return articleRepository.findByCultureId(cultureId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ArticleDto> getByPublisherId(Long publisherId) {
        return articleRepository.findByPublisherId(publisherId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ArticleDto> getByType(String type) {
        return articleRepository.findByType(type).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ArticleDto> getById(Long id) {
        return articleRepository.findById(id)
                .map(this::toDto);
    }

    public ArticleDto create(ArticleDto dto) {
        Article article = toEntity(dto);
        Culture culture = cultureRepository.findById(dto.getCultureId())
                .orElseThrow(() -> new RuntimeException("Culture not found"));
        Admin publisher = adminRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        
        article.setCulture(culture);
        article.setPublisher(publisher);
        return toDto(articleRepository.save(article));
    }

    public Optional<ArticleDto> update(Long id, ArticleDto dto) {
        return articleRepository.findById(id)
                .map(existing -> {
                    existing.setTitre(dto.getTitre());
                    existing.setType(dto.getType());
                    existing.setContent(dto.getContent());
                    existing.setDescription(dto.getDescription());
                    if (dto.getCultureId() != null) {
                        Culture culture = cultureRepository.findById(dto.getCultureId())
                                .orElseThrow(() -> new RuntimeException("Culture not found"));
                        existing.setCulture(culture);
                    }
                    return toDto(articleRepository.save(existing));
                });
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    public ArticleDto createArticle(ArticleDto articleDto, List<MultipartFile> mediaFiles) throws IOException {
        Article article = new Article();
        article.setTitre(articleDto.getTitre());
        article.setType(articleDto.getType());
        article.setContent(articleDto.getContent());
        article.setDescription(articleDto.getDescription());
        
        // Handle media files
        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            List<String> mediaUrls = new ArrayList<>();
            for (MultipartFile file : mediaFiles) {
                if (!file.isEmpty()) {
                    String filePath = fileStorageService.storeFile(file, "articles", article.getId().toString());
                    mediaUrls.add(filePath);
                }
            }
            article.setMediaUrls(mediaUrls);
        }

        Article savedArticle = articleRepository.save(article);
        return convertToDto(savedArticle);
    }

    public void deleteArticle(Long articleId) throws IOException {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new RuntimeException("Article not found"));

        // Delete associated media files
        if (article.getMediaUrls() != null) {
            for (String mediaUrl : article.getMediaUrls()) {
                fileStorageService.deleteFile(mediaUrl);
            }
        }

        articleRepository.delete(article);
    }

    private ArticleDto toDto(Article a) {
        ArticleDto dto = new ArticleDto();
        dto.setId(a.getId());
        dto.setTitre(a.getTitre());
        dto.setType(a.getType());
        dto.setContent(a.getContent());
        dto.setDescription(a.getDescription());
        dto.setCultureId(a.getCulture().getId());
        dto.setPublisherId(a.getPublisher().getId());
        dto.setPublishedAt(a.getPublishedAt());
        return dto;
    }

    private Article toEntity(ArticleDto dto) {
        Article a = new Article();
        a.setId(dto.getId());
        a.setTitre(dto.getTitre());
        a.setType(dto.getType());
        a.setContent(dto.getContent());
        a.setDescription(dto.getDescription());
        return a;
    }

    private ArticleDto convertToDto(Article article) {
        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitre(article.getTitre());
        dto.setType(article.getType());
        dto.setContent(article.getContent());
        dto.setDescription(article.getDescription());
        dto.setMediaUrls(article.getMediaUrls());
        dto.setCultureId(article.getCulture() != null ? article.getCulture().getId() : null);
        dto.setPublisherId(article.getPublisher() != null ? article.getPublisher().getId() : null);
        dto.setPublishedAt(article.getPublishedAt());
        return dto;
    }
} 