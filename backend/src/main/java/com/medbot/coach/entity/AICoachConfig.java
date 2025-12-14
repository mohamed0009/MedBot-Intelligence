package com.medbot.coach.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_coach_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AICoachConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "language", nullable = false)
    private String language = "Français";

    @Column(name = "response_tone", nullable = false)
    private String responseTone = "Amical"; // Amical, Professionnel, Formel

    @Column(name = "detail_level", nullable = false)
    private String detailLevel = "Modéré"; // Minimal, Modéré, Détaillé

    @Column(name = "max_response_length", nullable = false)
    private Integer maxResponseLength = 500;

    @Column(name = "enable_quiz_generation", nullable = false)
    private Boolean enableQuizGeneration = true;

    @Column(name = "enable_summary_generation", nullable = false)
    private Boolean enableSummaryGeneration = true;

    @Column(name = "enable_exercise_generation", nullable = false)
    private Boolean enableExerciseGeneration = true;

    @Column(name = "enable_advanced_personalization", nullable = false)
    private Boolean enableAdvancedPersonalization = true;

    @Column(name = "total_interactions", nullable = false)
    private Integer totalInteractions = 0;

    @Column(name = "average_response_time_ms", nullable = false)
    private Long averageResponseTimeMs = 0L;

    @Column(name = "reported_interactions", nullable = false)
    private Integer reportedInteractions = 0;

    @Column(name = "generated_content_count", nullable = false)
    private Integer generatedContentCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

