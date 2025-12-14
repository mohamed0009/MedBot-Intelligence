package com.medbot.trainer.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.trainer.entity.Trainer;
import com.medbot.trainer.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainers", description = "API pour la gestion des formateurs")
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    @Operation(summary = "Créer un nouveau formateur")
    public ResponseEntity<ApiResponse<Trainer>> createTrainer(@RequestBody Trainer trainer) {
        Trainer created = trainerService.createTrainer(trainer);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Formateur créé avec succès", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un formateur par son ID")
    public ResponseEntity<ApiResponse<Trainer>> getTrainerById(@PathVariable UUID id) {
        Trainer trainer = trainerService.getTrainerById(id);
        return ResponseEntity.ok(ApiResponse.success(trainer));
    }

    @GetMapping
    @Operation(summary = "Liste tous les formateurs")
    public ResponseEntity<ApiResponse<Page<Trainer>>> getAllTrainers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Trainer.TrainerStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Trainer> trainers;

        if (status != null) {
            trainers = trainerService.getTrainersByStatus(status, pageable);
        } else {
            trainers = trainerService.getAllTrainers(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(trainers));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques des formateurs")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTrainerStats() {
        Map<String, Object> stats = trainerService.getTrainerStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour un formateur")
    public ResponseEntity<ApiResponse<Trainer>> updateTrainer(
            @PathVariable UUID id,
            @RequestBody Trainer trainer) {
        Trainer updated = trainerService.updateTrainer(id, trainer);
        return ResponseEntity.ok(ApiResponse.success("Formateur mis à jour", updated));
    }

    @PostMapping("/{id}/validate")
    @Operation(summary = "Valider un formateur")
    public ResponseEntity<ApiResponse<Trainer>> validateTrainer(@PathVariable UUID id) {
        Trainer trainer = trainerService.validateTrainer(id);
        return ResponseEntity.ok(ApiResponse.success("Formateur validé", trainer));
    }

    @PostMapping("/{id}/suspend")
    @Operation(summary = "Suspendre un formateur")
    public ResponseEntity<ApiResponse<Trainer>> suspendTrainer(@PathVariable UUID id) {
        Trainer trainer = trainerService.suspendTrainer(id);
        return ResponseEntity.ok(ApiResponse.success("Formateur suspendu", trainer));
    }
}

