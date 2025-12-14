package com.medbot.deid.service;

import com.medbot.deid.dto.AnonymizeRequest;
import com.medbot.deid.dto.AnonymizeResponse;
import com.medbot.deid.entity.AnonymizationLog;
import com.medbot.deid.repository.AnonymizationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.medbot.common.config.RabbitMQConfig.*;

@Service
@RequiredArgsConstructor
public class DeidService {

    private final AnonymizationService anonymizationService;
    private final AnonymizationLogRepository logRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public AnonymizeResponse anonymize(AnonymizeRequest request) {
        AnonymizationService.AnonymizeResult result = anonymizationService.anonymize(
            request.getText(),
            request.getStrategy(),
            request.getPreserveMedical()
        );

        // Sauvegarder le log
        AnonymizationLog log = new AnonymizationLog();
        log.setOriginalText(request.getText());
        log.setAnonymizedText(result.anonymizedText());
        log.setStrategy(result.strategy());
        log.setDetectedEntities(result.detectedEntities());
        log.setPreserveMedical(request.getPreserveMedical());
        logRepository.save(log);

        return new AnonymizeResponse(
            result.anonymizedText(),
            result.detectedEntities(),
            result.strategy()
        );
    }

    public AnonymizeResponse analyze(String text) {
        // Utiliser directement le service de détection
        com.medbot.deid.service.PiiDetectionService detectionService = 
            new com.medbot.deid.service.PiiDetectionService();
        List<AnonymizationLog.DetectedEntity> entities = detectionService.detectPii(text);
        
        return new AnonymizeResponse(
            text, // Pas d'anonymisation, juste analyse
            entities,
            null
        );
    }

    @Transactional
    public void processDocumentAnonymization(UUID documentId, String content) {
        AnonymizeRequest request = new AnonymizeRequest();
        request.setText(content);
        request.setStrategy(AnonymizationLog.AnonymizationStrategy.SYNTHESIZE);
        request.setPreserveMedical(true);

        AnonymizeResponse response = anonymize(request);

        // Publier l'événement
        rabbitTemplate.convertAndSend(
            ANONYMIZATION_EXCHANGE,
            DOCUMENT_ANONYMIZED_ROUTING_KEY,
            documentId.toString()
        );
    }
}

