package com.medbot.search.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.search.dto.SearchRequest;
import com.medbot.search.dto.SearchResult;
import com.medbot.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "API pour la recherche sémantique")
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/semantic")
    @Operation(summary = "Recherche sémantique dans les documents")
    public ResponseEntity<ApiResponse<List<SearchResult>>> semanticSearch(
            @Valid @RequestBody SearchRequest request) {
        List<SearchResult> results = searchService.semanticSearch(request);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @PostMapping("/index/{documentId}")
    @Operation(summary = "Indexer un document pour la recherche")
    public ResponseEntity<ApiResponse<Void>> indexDocument(
            @PathVariable UUID documentId,
            @RequestBody String content) {
        searchService.indexDocument(documentId, content);
        return ResponseEntity.ok(ApiResponse.success("Document indexed successfully", null));
    }
}


