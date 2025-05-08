package com.aiwebapp.biosafeai.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDto {
    private Long id;
    private String titre;
    private String type;
    private String content;
    private String description;
    private List<String> mediaUrls;
    private Long cultureId;
    private Long publisherId;
    private LocalDateTime publishedAt;
} 