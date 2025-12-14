package com.medbot.synthesis.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.synthesis.dto.SynthesisRequest;
import com.medbot.synthesis.entity.Synthesis;
import com.medbot.synthesis.service.SynthesisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/synthesis")
@RequiredArgsConstructor
@Tag(name = "Synthesis", description = "API pour la synthèse comparative")
public class SynthesisController {

    private final SynthesisService synthesisService;

    @PostMapping("/patient/{patientId}")
    @Operation(summary = "Générer une synthèse pour un patient")
    public ResponseEntity<ApiResponse<Synthesis>> generateSynthesis(
            @PathVariable String patientId,
            @RequestParam(required = false, defaultValue = "SUMMARY") Synthesis.SynthesisType type) {
        SynthesisRequest request = new SynthesisRequest();
        request.setPatientId(patientId);
        request.setSynthesisType(type);
        
        Synthesis synthesis = synthesisService.generateSynthesis(request);
        return ResponseEntity.ok(ApiResponse.success("Synthesis generated successfully", synthesis));
    }

    @PostMapping("/timeline")
    @Operation(summary = "Générer une timeline pour un patient")
    public ResponseEntity<ApiResponse<Synthesis>> generateTimeline(
            @Valid @RequestBody SynthesisRequest request) {
        request.setSynthesisType(Synthesis.SynthesisType.TIMELINE);
        Synthesis synthesis = synthesisService.generateSynthesis(request);
        return ResponseEntity.ok(ApiResponse.success("Timeline generated successfully", synthesis));
    }
}


