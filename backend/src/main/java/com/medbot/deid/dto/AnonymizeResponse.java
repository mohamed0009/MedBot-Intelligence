package com.medbot.deid.dto;

import com.medbot.deid.entity.AnonymizationLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnonymizeResponse {
    private String anonymizedText;
    private List<AnonymizationLog.DetectedEntity> detectedEntities;
    private AnonymizationLog.AnonymizationStrategy strategy;
}


