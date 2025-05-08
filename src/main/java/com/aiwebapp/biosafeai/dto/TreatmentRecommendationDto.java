package com.aiwebapp.biosafeai.dto;

import lombok.Data;

public class TreatmentRecommendationDto {
    private String type; // e.g., "Eco-friendly", "Chemical"
    private String description;

    public TreatmentRecommendationDto() {
    }

    public TreatmentRecommendationDto(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}