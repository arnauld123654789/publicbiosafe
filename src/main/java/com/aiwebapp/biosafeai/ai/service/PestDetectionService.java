package com.aiwebapp.biosafeai.ai.service;

import com.aiwebapp.biosafeai.dto.DiagnosisDto;
import com.aiwebapp.biosafeai.dto.PestDetectionResultDto;
import com.aiwebapp.biosafeai.dto.TreatmentRecommendationDto;
import com.aiwebapp.biosafeai.enums.TreatmentType;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vertexai.api.GenerationConfig;

import com.google.protobuf.ByteString;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service

public class PestDetectionService {

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.region}")
    private String region;

    private VertexAI vertexAI;

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() throws IOException {
        // Initialize VertexAI client with API key
        this.vertexAI = new VertexAI(projectId, region, apiKey);
    }


    public PestDetectionResultDto detectPests(MultipartFile image) throws IOException {
        PestDetectionResultDto detectionResult = new PestDetectionResultDto();
        detectionResult.setTimestamp(getCurrentTimestamp());

        if (image.isEmpty()) {
            detectionResult.setErrorMessage("No image provided");
            return detectionResult;
        }

        try {


            GenerationConfig.Builder config = GenerationConfig.newBuilder();
            config.setMaxOutputTokens(1024);
            config.setTemperature(0.4f);
            config.setTopP(1);
            GenerativeModel model = new GenerativeModel("gemini-pro-vision", this.vertexAI);

            // Prepare image for Gemini API
            byte[] imageBytes = image.getBytes();
            // Debugging: Check if imageBytes are empty
            if (imageBytes.length == 0) {
                detectionResult.setErrorMessage("Empty image data received");
                return detectionResult;
            }

            Part imagePart = Part.newBuilder()
                    .setInlineData(com.google.cloud.vertexai.api.Part.InlineData.newBuilder()
                            .setData(ByteString.copyFrom(imageBytes))
                            .setMimeType(image.getContentType())
                            .build())
                    .build();

            String prompt = "Analyze the following image of a plant and identify any signs of pests or diseases. List the detected pests and diseases. Return only the pests or diseases detected. if there are no issues return only 'No issues detected'. also provide me with a diagnosis for each issues you found.";

            // Create the content with the prompt and image
            Content content = Content.newBuilder()
                    .addParts(Part.newBuilder().setText(prompt).build())
                    .addParts(imagePart)
                    .build();

            GenerateContentResponse response = model.generateContent(content, config.build());

            String responseText = ResponseHandler.getText(response);
            List<String> detectedIssues = new ArrayList<>();
            List<DiagnosisDto> diagnoses = new ArrayList<>();

            String[] lines = responseText.split("\\.");

            for (String line : lines) {
                line = line.trim();
                if (line.toLowerCase().contains("no issues detected")) {
                    detectionResult.setNoIssuesDetected(true);
                    return detectionResult;
                }
                if (!line.isEmpty()) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String issue = parts[0].trim();
                        String diagnosis = parts[1].trim();
                        detectedIssues.add(issue);
                        DiagnosisDto diag = new DiagnosisDto();
                        diag.setIssue(issue);
                        diag.setDiagnosis(diagnosis);
                        diagnoses.add(diag);
                    } else {
                        detectedIssues.add(line);
                        DiagnosisDto diag = new DiagnosisDto();
                        diag.setIssue(line);
                        diagnoses.add(diag);
                    }
                }

            }
            detectionResult.setDetectedIssues(detectedIssues);
            detectionResult.setDiagnoses(diagnoses);
            return detectionResult;
        } catch (Exception e) {
            e.printStackTrace();
            detectionResult.setErrorMessage(e.getMessage());
            return detectionResult;
        }
    }

    public List<TreatmentRecommendationDto> getTreatmentRecommendations(PestDetectionResultDto detectionResult) {
        List<TreatmentRecommendationDto> recommendations = new ArrayList<>();
        List<String> detectedPestsAndDiseases=new ArrayList<>();
        if(detectionResult.getDiagnoses()!=null){
            for (DiagnosisDto diag:detectionResult.getDiagnoses()) {
                detectedPestsAndDiseases.add(diag.getIssue());
            }
        }
        if (detectedPestsAndDiseases == null) {
            return Collections.emptyList();
        }

        // Use a map to store treatments for each issue
        Map<String, List<TreatmentRecommendationDto>> treatmentMap = new HashMap<>();
        treatmentMap.put("aphids", Arrays.asList(
                new TreatmentRecommendationDto("Use neem oil spray", TreatmentType.ECO_FRIENDLY),
                new TreatmentRecommendationDto("Apply an insecticide containing malathion", TreatmentType.CHEMICAL)
        ));
        treatmentMap.put("powdery mildew", Arrays.asList(
                new TreatmentRecommendationDto("Apply a baking soda solution", TreatmentType.ECO_FRIENDLY),
                new TreatmentRecommendationDto("Use a fungicide containing sulfur", TreatmentType.CHEMICAL)
        ));
        treatmentMap.put("leaf spot", Arrays.asList(
                new TreatmentRecommendationDto("Remove affected leaves and improve air circulation", TreatmentType.ECO_FRIENDLY),
                new TreatmentRecommendationDto("Apply a copper-based fungicide", TreatmentType.CHEMICAL)
        ));
        treatmentMap.put("spider mites", Arrays.asList(
                new TreatmentRecommendationDto("Spray with water or use insecticidal soap", TreatmentType.ECO_FRIENDLY),
                new TreatmentRecommendationDto("Apply a miticide", TreatmentType.CHEMICAL)
        ));
        treatmentMap.put("root rot", Arrays.asList(
                new TreatmentRecommendationDto("Improve drainage and reduce watering", TreatmentType.ECO_FRIENDLY),
                new TreatmentRecommendationDto("Use a fungicide drench", TreatmentType.CHEMICAL)
        ));

        for (String issue : detectedPestsAndDiseases) {
            String normalizedIssue = issue.trim().toLowerCase();
            List<TreatmentRecommendationDto> treatments = treatmentMap.get(normalizedIssue);
            if (treatments != null) {
                recommendations.addAll(treatments);
            } else {
                recommendations.add(new TreatmentRecommendationDto("Consult an expert for specific treatment for " + issue, "unknown"));
            }
        }
        if (recommendations.isEmpty()) {
            recommendations.add(new TreatmentRecommendationDto("No treatment is required", "none"));
        }
        return recommendations;
    }

    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}

class DateTimeUtil {

}

class DateTimeUtil {

    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}