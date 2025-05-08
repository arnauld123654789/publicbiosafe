package com.aiwebapp.biosafeai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String description;

    @ElementCollection
    @CollectionTable(name = "article_media_urls", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "media_url")
    @Builder.Default
    private List<String> mediaUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culture_id")
    private Culture culture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Admin publisher;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @PrePersist
    protected void onCreate() {
        publishedAt = LocalDateTime.now();
    }
} 