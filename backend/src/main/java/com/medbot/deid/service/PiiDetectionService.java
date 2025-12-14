package com.medbot.deid.service;

import com.medbot.deid.entity.AnonymizationLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PiiDetectionService {

    // Patterns pour détecter les PII
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("\\b(\\+?\\d{1,3}[-.]?)?\\(?\\d{3}\\)?[-.]?\\d{3}[-.]?\\d{4}\\b");
    
    private static final Pattern SSN_PATTERN = 
        Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b");
    
    private static final Pattern IP_PATTERN = 
        Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");

    public List<AnonymizationLog.DetectedEntity> detectPii(String text) {
        List<AnonymizationLog.DetectedEntity> entities = new ArrayList<>();
        
        // Détecter emails
        detectPattern(text, EMAIL_PATTERN, "EMAIL", entities);
        
        // Détecter téléphones
        detectPattern(text, PHONE_PATTERN, "PHONE", entities);
        
        // Détecter SSN
        detectPattern(text, SSN_PATTERN, "SSN", entities);
        
        // Détecter IP
        detectPattern(text, IP_PATTERN, "IP_ADDRESS", entities);
        
        // Détecter noms (simplifié - utiliser NLP en production)
        detectNames(text, entities);
        
        return entities;
    }

    private void detectPattern(String text, Pattern pattern, String type, 
                               List<AnonymizationLog.DetectedEntity> entities) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            entities.add(new AnonymizationLog.DetectedEntity(
                type,
                matcher.group(),
                0.9,
                matcher.start(),
                matcher.end()
            ));
        }
    }

    private void detectNames(String text, List<AnonymizationLog.DetectedEntity> entities) {
        // Pattern simplifié pour détecter les noms (Dr. X, M. Y, etc.)
        Pattern namePattern = Pattern.compile(
            "\\b(?:Dr\\.?|M\\.?|Mme|Mlle)\\s+([A-Z][a-z]+(?:\\s+[A-Z][a-z]+)*)"
        );
        Matcher matcher = namePattern.matcher(text);
        while (matcher.find()) {
            entities.add(new AnonymizationLog.DetectedEntity(
                "PERSON",
                matcher.group(1),
                0.7,
                matcher.start(),
                matcher.end()
            ));
        }
    }
}


