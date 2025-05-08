package com.aiwebapp.biosafeai.dto;

import java.util.List;
import java.util.ArrayList;

public class PestDetectionResultDto {

    private List<DiagnosisDto> detectedIssues;

    public PestDetectionResultDto(){
        this.detectedIssues = new ArrayList<>();
    }

    public List<DiagnosisDto> getDetectedIssues() {
        return detectedIssues;
    }

    public void setDetectedIssues(List<DiagnosisDto> detectedIssues){
        this.detectedIssues = detectedIssues;
    }
}