package com.aiwebapp.biosafeai.dto;

import java.util.List;

public class PestDetectionResponseDto {

    private PestDetectionResultDto detectionResult;
    private List<TreatmentRecommendationDto> treatmentRecommendations;

    public PestDetectionResultDto getDetectionResult() {
        return detectionResult;
    }

    public void setDetectionResult(PestDetectionResultDto detectionResult) {
        this.detectionResult = detectionResult;
    }

    public List<TreatmentRecommendationDto> getTreatmentRecommendations() {
        return treatmentRecommendations;
    }

    public void setTreatmentRecommendations(List<TreatmentRecommendationDto> treatmentRecommendations) {
        this.treatmentRecommendations = treatmentRecommendations;
    }
}