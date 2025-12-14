package com.medbot.document.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.document.dto.DocumentDTO;
import com.medbot.document.dto.DocumentUploadRequest;
import com.medbot.document.entity.DocumentMetadata;
import com.medbot.document.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "API pour la gestion des documents médicaux")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    @Operation(summary = "Upload un document médical")
    public ResponseEntity<ApiResponse<DocumentDTO>> uploadDocument(
            @Valid @ModelAttribute DocumentUploadRequest request) {
        DocumentDTO document = documentService.uploadDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Document uploaded successfully", document));
    }

    @GetMapping
    @Operation(summary = "Liste tous les documents avec pagination")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentDTO> documents = documentService.getAllDocuments(pageable);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un document par son ID")
    public ResponseEntity<ApiResponse<DocumentDTO>> getDocumentById(@PathVariable UUID id) {
        DocumentDTO document = documentService.getDocumentById(id);
        return ResponseEntity.ok(ApiResponse.success(document));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Récupère les documents d'un patient")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> getDocumentsByPatientId(
            @PathVariable String patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentDTO> documents = documentService.getDocumentsByPatientId(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Met à jour les métadonnées d'un document")
    public ResponseEntity<ApiResponse<DocumentDTO>> updateDocument(
            @PathVariable UUID id,
            @RequestBody(required = false) DocumentMetadata metadata,
            @RequestParam(required = false) String documentType) {
        DocumentDTO document = documentService.updateDocument(id, metadata, documentType);
        return ResponseEntity.ok(ApiResponse.success("Document updated successfully", document));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un document")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }
}


