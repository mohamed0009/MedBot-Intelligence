package com.medbot.search.service;

import com.medbot.search.dto.SearchResult;
import com.medbot.search.entity.DocumentChunk;
import com.medbot.search.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final DocumentChunkRepository chunkRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<SearchResult> searchSimilar(float[] queryEmbedding, int topK, double threshold) {
        // Récupérer tous les chunks (en production, utiliser pgvector pour recherche efficace)
        List<DocumentChunk> allChunks = chunkRepository.findAll();
        
        // Calculer la similarité cosinus
        List<SearchResult> results = allChunks.stream()
            .map(chunk -> {
                float[] chunkEmbedding = parseEmbedding(chunk.getEmbedding());
                double similarity = cosineSimilarity(queryEmbedding, chunkEmbedding);
                return new SearchResult(
                    chunk.getId(),
                    chunk.getDocumentId(),
                    chunk.getChunkText(),
                    similarity,
                    chunk.getChunkIndex(),
                    null // documentTitle à récupérer depuis Document
                );
            })
            .filter(result -> result.getSimilarityScore() >= threshold)
            .sorted(Comparator.comparing(SearchResult::getSimilarityScore).reversed())
            .limit(topK)
            .collect(Collectors.toList());
        
        return results;
    }

    private float[] parseEmbedding(String embeddingJson) {
        if (embeddingJson == null || embeddingJson.isEmpty()) {
            return new float[0];
        }
        try {
            return objectMapper.readValue(embeddingJson, new TypeReference<float[]>() {});
        } catch (Exception e) {
            return new float[0];
        }
    }

    private double cosineSimilarity(float[] a, float[] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0 || a.length != b.length) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        double denominator = Math.sqrt(normA) * Math.sqrt(normB);
        return denominator == 0.0 ? 0.0 : dotProduct / denominator;
    }
}

