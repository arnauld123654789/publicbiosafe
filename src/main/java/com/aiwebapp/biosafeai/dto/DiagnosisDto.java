package com.aiwebapp.biosafeai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
public class DiagnosisDto {
    private String diagnosedIssue;
    private double confidenceLevel;

    public DiagnosisDto() {
    }

    public DiagnosisDto(String diagnosedIssue, double confidenceLevel) {
        this.diagnosedIssue = diagnosedIssue;
        this.confidenceLevel = confidenceLevel;
    }

    public String getDiagnosedIssue() {
        return diagnosedIssue;
    }

    public void setDiagnosedIssue(String diagnosedIssue) {
        this.diagnosedIssue = diagnosedIssue;
    }

    public double getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(double confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
}