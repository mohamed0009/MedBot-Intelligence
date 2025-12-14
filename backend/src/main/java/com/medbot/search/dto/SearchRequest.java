package com.medbot.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchRequest {
    @NotBlank(message = "Query is required")
    private String query;
    
    private Integer topK = 10;
    private Double similarityThreshold = 0.7;
    private String patientId; // Optionnel pour filtrer par patient
}


