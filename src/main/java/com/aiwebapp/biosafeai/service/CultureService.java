package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.CultureDto;
import com.aiwebapp.biosafeai.entity.Culture;
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
public class CultureService {
    private final CultureRepository cultureRepository;
    private final FileStorageService fileStorageService;

    public List<CultureDto> getAll() {
        return cultureRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CultureDto> getByType(String type) {
        return cultureRepository.findByType(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CultureDto> getById(Long id) {
        return cultureRepository.findById(id)
                .map(this::convertToDto);
    }

    public CultureDto createCulture(CultureDto cultureDto, List<MultipartFile> mediaFiles) throws IOException {
        Culture culture = new Culture();
        culture.setName(cultureDto.getName());
        culture.setType(cultureDto.getType());
        culture.setPhotoUrl(cultureDto.getPhotoUrl());
        
        // Handle media files
        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            List<String> mediaUrls = new ArrayList<>();
            for (MultipartFile file : mediaFiles) {
                if (!file.isEmpty()) {
                    String filePath = fileStorageService.storeFile(file, "cultures", culture.getId().toString());
                    mediaUrls.add(filePath);
                }
            }
            culture.setMediaUrls(mediaUrls);
        }

        Culture savedCulture = cultureRepository.save(culture);
        return convertToDto(savedCulture);
    }

    public Optional<CultureDto> update(Long id, CultureDto dto) {
        return cultureRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setType(dto.getType());
                    existing.setPhotoUrl(dto.getPhotoUrl());
                    return convertToDto(cultureRepository.save(existing));
                });
    }

    public void deleteCulture(Long cultureId) throws IOException {
        Culture culture = cultureRepository.findById(cultureId)
            .orElseThrow(() -> new RuntimeException("Culture not found"));

        // Delete associated media files
        if (culture.getMediaUrls() != null) {
            for (String mediaUrl : culture.getMediaUrls()) {
                fileStorageService.deleteFile(mediaUrl);
            }
        }

        cultureRepository.delete(culture);
    }

    private CultureDto convertToDto(Culture culture) {
        CultureDto dto = new CultureDto();
        dto.setId(culture.getId());
        dto.setName(culture.getName());
        dto.setType(culture.getType());
        dto.setPhotoUrl(culture.getPhotoUrl());
        dto.setMediaUrls(culture.getMediaUrls());
        return dto;
    }
} 