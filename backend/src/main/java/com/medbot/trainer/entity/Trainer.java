package com.medbot.trainer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trainers", indexes = {
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills; // JSON array ou comma-separated

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainerStatus status = TrainerStatus.EN_ATTENTE;

    @Column(name = "students_count", nullable = false)
    private Integer studentsCount = 0;

    @Column(name = "courses_count", nullable = false)
    private Integer coursesCount = 0;

    @Column(name = "rating", nullable = false)
    private Double rating = 0.0;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TrainerStatus {
        EN_ATTENTE,
        ACTIF,
        SUSPENDU
    }
}

