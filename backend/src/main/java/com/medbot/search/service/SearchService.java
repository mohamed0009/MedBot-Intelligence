package com.medbot.search.service;

import com.medbot.search.dto.SearchRequest;
import com.medbot.search.dto.SearchResult;
import com.medbot.search.entity.DocumentChunk;
import com.medbot.search.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static com.medbot.common.config.RabbitMQConfig.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final EmbeddingService embeddingService;
    private final ChunkingService chunkingService;
    private final VectorSearchService vectorSearchService;
    private final DocumentChunkRepository chunkRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void indexDocument(UUID documentId, String content) {
        // Supprimer les anciens chunks
        chunkRepository.deleteByDocumentId(documentId);

        // Découper le document
        List<ChunkingService.Chunk> chunks = chunkingService.chunkText(content);

        // Générer les embeddings et sauvegarder
        for (ChunkingService.Chunk chunk : chunks) {
            float[] embedding = embeddingService.generateEmbedding(chunk.text());
            
            // Convertir float[] en JSON string
            String embeddingJson;
            try {
                embeddingJson = objectMapper.writeValueAsString(embedding);
            } catch (Exception e) {
                embeddingJson = "[]";
            }

            DocumentChunk documentChunk = new DocumentChunk();
            documentChunk.setDocumentId(documentId);
            documentChunk.setChunkText(chunk.text());
            documentChunk.setChunkIndex(chunk.index());
            documentChunk.setEmbedding(embeddingJson);

            chunkRepository.save(documentChunk);
        }

        // Publier l'événement
        rabbitTemplate.convertAndSend(
            INDEXING_EXCHANGE,
            DOCUMENT_INDEXED_ROUTING_KEY,
            documentId.toString()
        );
    }

    public List<SearchResult> semanticSearch(SearchRequest request) {
        // Générer l'embedding de la requête
        float[] queryEmbedding = embeddingService.generateEmbedding(request.getQuery());

        // Recherche vectorielle
        List<SearchResult> results = vectorSearchService.searchSimilar(
            queryEmbedding,
            request.getTopK(),
            request.getSimilarityThreshold()
        );

        // Filtrer par patient si spécifié
        if (request.getPatientId() != null) {
            // TODO: Filtrer par patientId en joignant avec Document
        }

        return results;
    }
}

