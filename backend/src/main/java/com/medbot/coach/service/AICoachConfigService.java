package com.medbot.coach.service;

import com.medbot.coach.entity.AICoachConfig;
import com.medbot.coach.repository.AICoachConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AICoachConfigService {

    private final AICoachConfigRepository configRepository;

    @Transactional(readOnly = true)
    public AICoachConfig getConfig() {
        AICoachConfig config = configRepository.findFirstByOrderByCreatedAtDesc();
        if (config == null) {
            // Créer une configuration par défaut
            config = new AICoachConfig();
            config = configRepository.save(config);
        }
        return config;
    }

    @Transactional
    public AICoachConfig updateConfig(AICoachConfig config) {
        AICoachConfig existing = getConfig();
        
        if (config.getLanguage() != null) existing.setLanguage(config.getLanguage());
        if (config.getResponseTone() != null) existing.setResponseTone(config.getResponseTone());
        if (config.getDetailLevel() != null) existing.setDetailLevel(config.getDetailLevel());
        if (config.getMaxResponseLength() != null) existing.setMaxResponseLength(config.getMaxResponseLength());
        if (config.getEnableQuizGeneration() != null) existing.setEnableQuizGeneration(config.getEnableQuizGeneration());
        if (config.getEnableSummaryGeneration() != null) existing.setEnableSummaryGeneration(config.getEnableSummaryGeneration());
        if (config.getEnableExerciseGeneration() != null) existing.setEnableExerciseGeneration(config.getEnableExerciseGeneration());
        if (config.getEnableAdvancedPersonalization() != null) existing.setEnableAdvancedPersonalization(config.getEnableAdvancedPersonalization());

        return configRepository.save(existing);
    }

    @Transactional
    public AICoachConfig incrementInteractions() {
        AICoachConfig config = getConfig();
        config.setTotalInteractions(config.getTotalInteractions() + 1);
        return configRepository.save(config);
    }

    @Transactional
    public AICoachConfig updateResponseTime(Long responseTimeMs) {
        AICoachConfig config = getConfig();
        long totalTime = config.getAverageResponseTimeMs() * config.getTotalInteractions() + responseTimeMs;
        long newAverage = totalTime / (config.getTotalInteractions() + 1);
        config.setAverageResponseTimeMs(newAverage);
        return configRepository.save(config);
    }

    @Transactional
    public AICoachConfig incrementGeneratedContent() {
        AICoachConfig config = getConfig();
        config.setGeneratedContentCount(config.getGeneratedContentCount() + 1);
        return configRepository.save(config);
    }
}

