package com.medbot.deid.service;

import com.medbot.deid.entity.AnonymizationLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AnonymizationService {

    private final PiiDetectionService piiDetectionService;
    private final MedicalEntityPreservationService medicalPreservationService;
    private final Random random = new Random();

    public AnonymizeResult anonymize(String text, 
                                     AnonymizationLog.AnonymizationStrategy strategy,
                                     boolean preserveMedical) {
        // Détecter les entités PII
        List<AnonymizationLog.DetectedEntity> entities = piiDetectionService.detectPii(text);
        
        // Filtrer les entités médicales si nécessaire
        if (preserveMedical) {
            entities = medicalPreservationService.filterMedicalEntities(text, entities);
        }
        
        // Anonymiser selon la stratégie
        String anonymizedText = applyStrategy(text, entities, strategy);
        
        return new AnonymizeResult(anonymizedText, entities, strategy);
    }

    private String applyStrategy(String text, 
                                 List<AnonymizationLog.DetectedEntity> entities,
                                 AnonymizationLog.AnonymizationStrategy strategy) {
        String result = text;
        
        // Trier par position décroissante pour éviter les problèmes d'index
        entities.sort((a, b) -> Integer.compare(b.getStart(), a.getStart()));
        
        for (AnonymizationLog.DetectedEntity entity : entities) {
            String replacement = switch (strategy) {
                case REDACTION -> "[REDACTED]";
                case REPLACEMENT -> generateReplacement(entity.getType());
                case HASHING -> hashValue(entity.getValue());
                case SYNTHESIZE -> generateSyntheticValue(entity.getType());
            };
            
            result = result.substring(0, entity.getStart()) + 
                    replacement + 
                    result.substring(entity.getEnd());
        }
        
        return result;
    }

    private String generateReplacement(String type) {
        return switch (type) {
            case "EMAIL" -> "email@example.com";
            case "PHONE" -> "[PHONE]";
            case "SSN" -> "[SSN]";
            case "PERSON" -> "[PATIENT]";
            default -> "[REDACTED]";
        };
    }

    private String hashValue(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.substring(0, 8); // Retourner les 8 premiers caractères
        } catch (Exception e) {
            return "[HASHED]";
        }
    }

    private String generateSyntheticValue(String type) {
        return switch (type) {
            case "EMAIL" -> "patient" + random.nextInt(1000) + "@hospital.com";
            case "PHONE" -> "+33-" + (random.nextInt(900) + 100) + "-" + 
                           (random.nextInt(900) + 100) + "-" + (random.nextInt(9000) + 1000);
            case "SSN" -> String.format("%03d-%02d-%04d", 
                           random.nextInt(900) + 100, 
                           random.nextInt(90) + 10, 
                           random.nextInt(9000) + 1000);
            case "PERSON" -> "Patient " + (char)('A' + random.nextInt(26)) + 
                           (char)('a' + random.nextInt(26));
            default -> "[SYNTHETIC]";
        };
    }

    public record AnonymizeResult(
        String anonymizedText,
        List<AnonymizationLog.DetectedEntity> detectedEntities,
        AnonymizationLog.AnonymizationStrategy strategy
    ) {}
}


