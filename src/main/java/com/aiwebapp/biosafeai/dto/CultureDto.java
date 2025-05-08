package com.aiwebapp.biosafeai.dto;

import lombok.Data;

import java.util.List;

@Data
public class CultureDto {
    private Long id;
    private String name;
    private String type;
    private List<String> mediaUrls;
    private String photoUrl;
} 