package com.medbot.audit.controller;

import com.medbot.audit.entity.AuditLog;
import com.medbot.audit.repository.AuditLogRepository;
import com.medbot.audit.service.AuditService;
import com.medbot.common.dto.ApiResponse;
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
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Tag(name = "Audit", description = "API pour les logs d'audit")
public class AuditController {

    private final AuditLogRepository auditLogRepository;
    private final AuditService auditService;

    @GetMapping("/logs")
    @Operation(summary = "Obtenir les logs d'audit")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getLogs(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> logs;
        
        if (userId != null) {
            logs = auditLogRepository.findByUserId(userId, pageable);
        } else if (action != null) {
            logs = auditLogRepository.findByAction(action, pageable);
        } else {
            logs = auditLogRepository.findAll(pageable);
        }
        
        return ResponseEntity.ok(ApiResponse.success(logs));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtenir les logs d'un utilisateur")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getLogsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> logs = auditLogRepository.findByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }

    @GetMapping("/document/{documentId}")
    @Operation(summary = "Obtenir les logs d'un document")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getLogsByDocument(
            @PathVariable UUID documentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> logs = auditLogRepository.findByResourceTypeAndResourceId("DOCUMENT", documentId, pageable);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }
}


