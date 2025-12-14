package com.medbot.coach.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.coach.entity.AICoachConfig;
import com.medbot.coach.service.AICoachConfigService;
import com.medbot.qa.entity.Question;
import com.medbot.qa.repository.QuestionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/coach")
@RequiredArgsConstructor
@Tag(name = "AI Coach", description = "API pour la supervision et configuration du coach virtuel IA")
public class AICoachConfigController {

    private final AICoachConfigService configService;
    private final QuestionRepository questionRepository;

    @GetMapping("/config")
    @Operation(summary = "Obtenir la configuration du coach IA")
    public ResponseEntity<ApiResponse<AICoachConfig>> getConfig() {
        AICoachConfig config = configService.getConfig();
        return ResponseEntity.ok(ApiResponse.success(config));
    }

    @PatchMapping("/config")
    @Operation(summary = "Mettre à jour la configuration du coach IA")
    public ResponseEntity<ApiResponse<AICoachConfig>> updateConfig(@RequestBody AICoachConfig config) {
        AICoachConfig updated = configService.updateConfig(config);
        return ResponseEntity.ok(ApiResponse.success("Configuration mise à jour", updated));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques du coach IA")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        AICoachConfig config = configService.getConfig();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInteractions", config.getTotalInteractions());
        stats.put("averageResponseTimeMs", config.getAverageResponseTimeMs());
        stats.put("reportedInteractions", config.getReportedInteractions());
        stats.put("generatedContentCount", config.getGeneratedContentCount());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/interactions")
    @Operation(summary = "Liste des interactions avec le coach IA")
    public ResponseEntity<ApiResponse<Page<Question>>> getInteractions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> interactions = questionRepository.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(interactions));
    }

    @GetMapping("/generated-content")
    @Operation(summary = "Liste du contenu généré par l'IA")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGeneratedContent() {
        // Pour l'instant, retourner les statistiques
        // TODO: Implémenter la liste des quiz, résumés, exercices générés
        Map<String, Object> content = new HashMap<>();
        AICoachConfig config = configService.getConfig();
        content.put("totalGenerated", config.getGeneratedContentCount());
        content.put("quizEnabled", config.getEnableQuizGeneration());
        content.put("summaryEnabled", config.getEnableSummaryGeneration());
        content.put("exerciseEnabled", config.getEnableExerciseGeneration());
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @GetMapping("/knowledge-base")
    @Operation(summary = "Informations sur la base de connaissances")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getKnowledgeBase() {
        Map<String, Object> kb = new HashMap<>();
        // TODO: Implémenter les statistiques de la base de connaissances
        kb.put("totalDocuments", 0);
        kb.put("totalChunks", 0);
        kb.put("lastUpdate", null);
        return ResponseEntity.ok(ApiResponse.success(kb));
    }
}

