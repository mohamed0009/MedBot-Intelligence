package com.medbot.deid.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.deid.dto.AnonymizeRequest;
import com.medbot.deid.dto.AnonymizeResponse;
import com.medbot.deid.entity.AnonymizationLog;
import com.medbot.deid.service.DeidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/anonymization")
@RequiredArgsConstructor
@Tag(name = "Anonymization", description = "API pour l'anonymisation des documents médicaux")
public class AnonymizationController {

    private final DeidService deidService;

    @PostMapping("/anonymize")
    @Operation(summary = "Anonymiser un texte")
    public ResponseEntity<ApiResponse<AnonymizeResponse>> anonymize(
            @Valid @RequestBody AnonymizeRequest request) {
        AnonymizeResponse response = deidService.anonymize(request);
        return ResponseEntity.ok(ApiResponse.success("Text anonymized successfully", response));
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyser un texte pour détecter les PII sans anonymiser")
    public ResponseEntity<ApiResponse<AnonymizeResponse>> analyze(@RequestBody String text) {
        AnonymizeResponse response = deidService.analyze(text);
        return ResponseEntity.ok(ApiResponse.success("Analysis completed", response));
    }

    @GetMapping("/strategies")
    @Operation(summary = "Obtenir la liste des stratégies d'anonymisation disponibles")
    public ResponseEntity<ApiResponse<List<String>>> getStrategies() {
        List<String> strategies = Arrays.stream(AnonymizationLog.AnonymizationStrategy.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(strategies));
    }

    @GetMapping("/entities")
    @Operation(summary = "Obtenir la liste des types d'entités détectables")
    public ResponseEntity<ApiResponse<List<String>>> getEntityTypes() {
        List<String> entities = List.of("EMAIL", "PHONE", "SSN", "IP_ADDRESS", "PERSON");
        return ResponseEntity.ok(ApiResponse.success(entities));
    }
}


