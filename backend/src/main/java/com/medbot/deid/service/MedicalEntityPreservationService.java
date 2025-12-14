package com.medbot.deid.service;

import com.medbot.deid.entity.AnonymizationLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicalEntityPreservationService {

    // Whitelist de termes médicaux à préserver
    private static final Set<String> MEDICAL_TERMS = Set.of(
        "diabète", "diabetes", "hypertension", "asthme", "asthma",
        "métabolique", "metabolic", "cardiaque", "cardiac", "pulmonaire",
        "pulmonary", "hépatique", "hepatic", "rénal", "renal",
        "médicament", "medication", "traitement", "treatment", "thérapie",
        "therapy", "diagnostic", "diagnosis", "symptôme", "symptom",
        "pathologie", "pathology", "syndrome", "maladie", "disease"
    );

    public List<AnonymizationLog.DetectedEntity> filterMedicalEntities(
            String text, List<AnonymizationLog.DetectedEntity> entities) {
        
        List<AnonymizationLog.DetectedEntity> filtered = new ArrayList<>();
        
        for (AnonymizationLog.DetectedEntity entity : entities) {
            // Extraire le contexte autour de l'entité
            String context = extractContext(text, entity.getStart(), entity.getEnd());
            
            // Vérifier si le contexte contient des termes médicaux
            if (!containsMedicalTerms(context)) {
                filtered.add(entity);
            }
            // Sinon, on ne l'ajoute pas (on préserve l'entité)
        }
        
        return filtered;
    }

    private String extractContext(String text, int start, int end) {
        int contextSize = 50;
        int contextStart = Math.max(0, start - contextSize);
        int contextEnd = Math.min(text.length(), end + contextSize);
        return text.substring(contextStart, contextEnd).toLowerCase();
    }

    private boolean containsMedicalTerms(String context) {
        return MEDICAL_TERMS.stream().anyMatch(context::contains);
    }
}


