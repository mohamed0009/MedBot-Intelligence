package com.medbot.qa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionRequest {
    @NotBlank(message = "Question is required")
    private String question;
    
    private String patientId;
    private String userId;
    private Integer maxSources = 5;
}


