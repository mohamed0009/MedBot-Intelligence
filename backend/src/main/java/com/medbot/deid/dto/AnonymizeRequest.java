package com.medbot.deid.dto;

import com.medbot.deid.entity.AnonymizationLog;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnonymizeRequest {
    @NotBlank(message = "Text is required")
    private String text;
    
    private AnonymizationLog.AnonymizationStrategy strategy = 
        AnonymizationLog.AnonymizationStrategy.SYNTHESIZE;
    
    private Boolean preserveMedical = true;
}


