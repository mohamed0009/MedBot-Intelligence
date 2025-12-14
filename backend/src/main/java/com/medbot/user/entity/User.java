package com.medbot.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_role", columnList = "role")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.APPRENANT;

    @Column(name = "formation")
    private String formation; // Domaine de formation

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private Niveau niveau; // Débutant, Intermédiaire, Avancé

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIF;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "phone")
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum UserRole {
        ADMINISTRATEUR,
        FORMATEUR,
        APPRENANT
    }

    public enum Niveau {
        DEBUTANT,
        INTERMEDIAIRE,
        AVANCE
    }

    public enum UserStatus {
        ACTIF,
        INACTIF,
        SUSPENDU
    }
}

