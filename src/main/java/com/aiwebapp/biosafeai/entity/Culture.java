package com.aiwebapp.biosafeai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cultures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Culture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @ElementCollection
    @CollectionTable(name = "culture_media_urls", joinColumns = @JoinColumn(name = "culture_id"))
    @Column(name = "media_url")
    @Builder.Default
    private List<String> mediaUrls = new ArrayList<>();

    @Column(name = "photo_url")
    private String photoUrl;
} 