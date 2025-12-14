package com.medbot.qa.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.qa.dto.QuestionRequest;
import com.medbot.qa.dto.QuestionResponse;
import com.medbot.qa.entity.Question;
import com.medbot.qa.repository.QuestionRepository;
import com.medbot.qa.service.RagPipelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qa")
@RequiredArgsConstructor
@Tag(name = "Q&A", description = "API pour les questions-réponses avec LLM")
public class QaController {

    private final RagPipelineService ragPipelineService;
    private final QuestionRepository questionRepository;

    @PostMapping("/ask")
    @Operation(summary = "Poser une question et obtenir une réponse")
    public ResponseEntity<ApiResponse<QuestionResponse>> askQuestion(
            @Valid @RequestBody QuestionRequest request) {
        QuestionResponse response = ragPipelineService.askQuestion(request);
        return ResponseEntity.ok(ApiResponse.success("Answer generated successfully", response));
    }

    @PostMapping(value = "/ask/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Poser une question avec streaming de la réponse")
    public SseEmitter askQuestionStream(@Valid @RequestBody QuestionRequest request) {
        SseEmitter emitter = new SseEmitter(60000L); // 60 secondes timeout
        
        // TODO: Implémenter le streaming avec le LLM
        try {
            QuestionResponse response = ragPipelineService.askQuestion(request);
            emitter.send(SseEmitter.event()
                .name("answer")
                .data(response.getAnswer()));
            emitter.complete();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        
        return emitter;
    }

    @GetMapping("/history")
    @Operation(summary = "Obtenir l'historique des questions")
    public ResponseEntity<ApiResponse<Page<Question>>> getHistory(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions;
        
        if (patientId != null) {
            questions = questionRepository.findByPatientId(patientId, pageable);
        } else if (userId != null) {
            questions = questionRepository.findByUserId(userId, pageable);
        } else {
            questions = questionRepository.findAll(pageable);
        }
        
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    @GetMapping("/history/{patientId}")
    @Operation(summary = "Obtenir l'historique des questions pour un patient")
    public ResponseEntity<ApiResponse<Page<Question>>> getHistoryByPatient(
            @PathVariable String patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionRepository.findByPatientIdOrderByCreatedAtDesc(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(questions));
    }
}


