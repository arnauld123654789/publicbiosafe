package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.ai.service.PestDetectionService;
import com.aiwebapp.biosafeai.dto.PestDetectionResponseDto;
import com.aiwebapp.biosafeai.dto.PestDetectionResultDto;
import com.aiwebapp.biosafeai.dto.TreatmentRecommendationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PestDetectionController {

    private final PestDetectionService pestDetectionService;

    @Autowired
    public PestDetectionController(PestDetectionService pestDetectionService) {
        this.pestDetectionService = pestDetectionService;
    }

    @PostMapping("/detect-pest")
    public ResponseEntity<?> detectPestAndRecommendTreatment(@RequestParam("image") MultipartFile image)  {
        try {
            PestDetectionResultDto detectionResult = pestDetectionService.detectPests(image);
            if (detectionResult.getDetectedIssues().isEmpty()){
               return ResponseEntity.ok("No pest or disease detected");
            }
            List<TreatmentRecommendationDto> recommendations = pestDetectionService.getTreatmentRecommendations(detectionResult);
            PestDetectionResponseDto responseDto = new PestDetectionResponseDto(detectionResult, recommendations);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error processing the image: " + e.getMessage());
        }
    }

}