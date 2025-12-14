package com.medbot.synthesis.service;

import com.medbot.document.entity.Document;
import com.medbot.document.repository.DocumentRepository;
import com.medbot.synthesis.dto.SynthesisRequest;
import com.medbot.synthesis.entity.Synthesis;
import com.medbot.synthesis.repository.SynthesisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SynthesisService {

    private final SynthesisRepository synthesisRepository;
    private final DocumentRepository documentRepository;
    private final TimelineService timelineService;

    @Transactional
    public Synthesis generateSynthesis(SynthesisRequest request) {
        // Récupérer les documents du patient
        List<Document> documents = documentRepository.findByPatientIdAndStatus(
            request.getPatientId(), 
            Document.DocumentStatus.INDEXED
        );

        String content;
        Map<String, Object> timeline = null;

        switch (request.getSynthesisType()) {
            case TIMELINE:
                timeline = timelineService.generateTimeline(documents);
                content = formatTimeline(timeline);
                break;
            case SUMMARY:
                content = generateSummary(documents);
                break;
            case EVOLUTION:
                content = generateEvolution(documents);
                break;
            default:
                content = generateSummary(documents);
        }

        Synthesis synthesis = new Synthesis();
        synthesis.setPatientId(request.getPatientId());
        synthesis.setSynthesisType(request.getSynthesisType());
        synthesis.setContent(content);
        synthesis.setTimeline(timeline);
        synthesis.setMetadata(extractMetadata(documents));

        return synthesisRepository.save(synthesis);
    }

    private String generateSummary(List<Document> documents) {
        return "Résumé des documents du patient:\n\n" +
               documents.stream()
                   .map(doc -> "- " + doc.getFilename() + " (" + doc.getDocumentType() + ")")
                   .collect(Collectors.joining("\n"));
    }

    private String generateEvolution(List<Document> documents) {
        return "Évolution du patient basée sur " + documents.size() + " documents.";
    }

    private String formatTimeline(Map<String, Object> timeline) {
        return "Timeline générée avec " + timeline.size() + " événements.";
    }

    private Map<String, Object> extractMetadata(List<Document> documents) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("documentCount", documents.size());
        metadata.put("documentTypes", documents.stream()
            .map(Document::getDocumentType)
            .distinct()
            .collect(Collectors.toList()));
        return metadata;
    }
}

