package com.medbot.qa.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    @Value("${medbot.llm.provider:openai}")
    private String provider;

    @Value("${medbot.llm.api-key:}")
    private String apiKey;

    @Value("${medbot.llm.model:gpt-4}")
    private String model;

    @Value("${medbot.llm.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    private final WebClient webClient;

    public LlmService() {
        this.webClient = WebClient.builder().build();
    }

    public String generateAnswer(String question, String context) {
        if (provider.equals("openai") && !apiKey.isEmpty()) {
            return generateOpenAIAnswer(question, context);
        } else {
            // Fallback: réponse simple
            return generateSimpleAnswer(question, context);
        }
    }

    public Flux<String> generateAnswerStream(String question, String context) {
        // TODO: Implémenter le streaming avec SSE
        return Flux.just(generateAnswer(question, context));
    }

    private String generateOpenAIAnswer(String question, String context) {
        try {
            String systemPrompt = "You are a medical assistant. Answer questions based on the provided medical context. " +
                    "Always cite your sources. If the answer is not in the context, say so.";

            Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", "Context: " + context + "\n\nQuestion: " + question)
                ),
                "temperature", 0.7,
                "max_tokens", 2000
            );

            var response = webClient.post()
                .uri(baseUrl + "/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            // Fallback
        }
        return generateSimpleAnswer(question, context);
    }

    private String generateSimpleAnswer(String question, String context) {
        return "Based on the provided context: " + context.substring(0, Math.min(200, context.length())) + 
               "... The answer to your question '" + question + "' would require more detailed analysis.";
    }
}


