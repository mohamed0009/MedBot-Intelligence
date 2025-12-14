package com.medbot.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "courses", indexes = {
    @Index(name = "idx_formation_id", columnList = "formation_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "formation_id")
    private UUID formationId; // Référence à la formation parente

    @Column(name = "formateur_id")
    private UUID formateurId; // ID du formateur

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status = CourseStatus.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private Niveau niveau; // Débutant, Intermédiaire, Avancé

    @Column(name = "duree_heures")
    private Integer dureeHeures;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "participants_count", nullable = false)
    private Integer participantsCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum CourseStatus {
        EN_ATTENTE,
        APPROUVE,
        REJETE,
        PUBLIE
    }

    public enum Niveau {
        DEBUTANT,
        INTERMEDIAIRE,
        AVANCE
    }
}

