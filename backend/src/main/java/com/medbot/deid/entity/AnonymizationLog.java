package com.medbot.deid.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "anonymization_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnonymizationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "document_id")
    private UUID documentId;

    @Column(name = "original_text", columnDefinition = "TEXT")
    private String originalText;

    @Column(name = "anonymized_text", columnDefinition = "TEXT")
    private String anonymizedText;

    @Column(name = "strategy", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnonymizationStrategy strategy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "detected_entities", columnDefinition = "jsonb")
    private List<DetectedEntity> detectedEntities;

    @Column(name = "preserve_medical")
    private Boolean preserveMedical = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum AnonymizationStrategy {
        REDACTION, REPLACEMENT, HASHING, SYNTHESIZE
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetectedEntity {
        private String type; // PERSON, EMAIL, PHONE, etc.
        private String value;
        private Double confidence;
        private Integer start;
        private Integer end;
    }
}


