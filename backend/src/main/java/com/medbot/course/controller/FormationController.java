package com.medbot.course.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.course.entity.Formation;
import com.medbot.course.service.FormationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/formations")
@RequiredArgsConstructor
@Tag(name = "Formations", description = "API pour la gestion des formations")
public class FormationController {

    private final FormationService formationService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle formation")
    public ResponseEntity<ApiResponse<Formation>> createFormation(@RequestBody Formation formation) {
        Formation created = formationService.createFormation(formation);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Formation créée avec succès", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une formation par son ID")
    public ResponseEntity<ApiResponse<Formation>> getFormationById(@PathVariable UUID id) {
        Formation formation = formationService.getFormationById(id);
        return ResponseEntity.ok(ApiResponse.success(formation));
    }

    @GetMapping
    @Operation(summary = "Liste toutes les formations")
    public ResponseEntity<ApiResponse<Page<Formation>>> getAllFormations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Formation> formations = formationService.getAllFormations(pageable);
        return ResponseEntity.ok(ApiResponse.success(formations));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour une formation")
    public ResponseEntity<ApiResponse<Formation>> updateFormation(
            @PathVariable UUID id,
            @RequestBody Formation formation) {
        Formation updated = formationService.updateFormation(id, formation);
        return ResponseEntity.ok(ApiResponse.success("Formation mise à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une formation")
    public ResponseEntity<ApiResponse<Void>> deleteFormation(@PathVariable UUID id) {
        formationService.deleteFormation(id);
        return ResponseEntity.ok(ApiResponse.success("Formation supprimée avec succès", null));
    }
}

