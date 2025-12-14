package com.medbot.search.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    @Value("${medbot.embedding.provider:openai}")
    private String provider;

    @Value("${medbot.embedding.api-key:}")
    private String apiKey;

    @Value("${medbot.embedding.model:text-embedding-3-small}")
    private String model;

    private final WebClient webClient;

    public EmbeddingService() {
        this.webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .build();
    }

    public float[] generateEmbedding(String text) {
        if (provider.equals("openai") && !apiKey.isEmpty()) {
            return generateOpenAIEmbedding(text);
        } else {
            // Fallback: simple TF-IDF ou embedding local
            return generateSimpleEmbedding(text);
        }
    }

    private float[] generateOpenAIEmbedding(String text) {
        try {
            Map<String, Object> requestBody = Map.of(
                "input", text,
                "model", model
            );

            Mono<Map> response = webClient.post()
                .uri("/embeddings")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class);

            Map<String, Object> result = response.block();
            if (result != null && result.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");
                if (!data.isEmpty()) {
                    List<Double> embedding = (List<Double>) data.get(0).get("embedding");
                    float[] floatArray = new float[embedding.size()];
                    for (int i = 0; i < embedding.size(); i++) {
                        floatArray[i] = embedding.get(i).floatValue();
                    }
                    return floatArray;
                }
            }
        } catch (Exception e) {
            // Fallback to simple embedding
        }
        return generateSimpleEmbedding(text);
    }

    private float[] generateSimpleEmbedding(String text) {
        // Embedding simple basé sur la longueur et les mots (à remplacer par un vrai modèle)
        int dimension = 1536;
        float[] embedding = new float[dimension];
        String[] words = text.toLowerCase().split("\\s+");
        
        for (int i = 0; i < Math.min(dimension, words.length); i++) {
            embedding[i] = words[i].hashCode() / 1000000f;
        }
        
        return embedding;
    }
}

