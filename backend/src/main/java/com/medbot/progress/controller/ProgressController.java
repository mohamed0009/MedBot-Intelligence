package com.medbot.progress.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.progress.entity.Progress;
import com.medbot.progress.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
@Tag(name = "Progress", description = "API pour le suivi de progression")
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping
    @Operation(summary = "Créer ou mettre à jour une progression")
    public ResponseEntity<ApiResponse<Progress>> createOrUpdateProgress(
            @RequestParam UUID userId,
            @RequestParam UUID courseId,
            @RequestParam Integer modulesCompleted,
            @RequestParam Integer totalModules,
            @RequestParam(required = false, defaultValue = "0") Integer timeSpentMinutes) {
        Progress progress = progressService.createOrUpdateProgress(
            userId, courseId, modulesCompleted, totalModules, timeSpentMinutes);
        return ResponseEntity.ok(ApiResponse.success("Progression mise à jour", progress));
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    @Operation(summary = "Obtenir la progression d'un utilisateur pour un cours")
    public ResponseEntity<ApiResponse<Progress>> getProgress(
            @PathVariable UUID userId,
            @PathVariable UUID courseId) {
        Progress progress = progressService.getProgressByUserAndCourse(userId, courseId);
        return ResponseEntity.ok(ApiResponse.success(progress));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtenir toutes les progressions d'un utilisateur")
    public ResponseEntity<ApiResponse<Page<Progress>>> getProgressByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Progress> progress = progressService.getProgressByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(progress));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Obtenir toutes les progressions pour un cours")
    public ResponseEntity<ApiResponse<Page<Progress>>> getProgressByCourse(
            @PathVariable UUID courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Progress> progress = progressService.getProgressByCourse(courseId, pageable);
        return ResponseEntity.ok(ApiResponse.success(progress));
    }
}

