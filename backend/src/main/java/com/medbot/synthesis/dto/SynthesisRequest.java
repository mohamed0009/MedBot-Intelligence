package com.medbot.synthesis.dto;

import com.medbot.synthesis.entity.Synthesis;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SynthesisRequest {
    @NotBlank(message = "Patient ID is required")
    private String patientId;
    
    private Synthesis.SynthesisType synthesisType = Synthesis.SynthesisType.SUMMARY;
}


