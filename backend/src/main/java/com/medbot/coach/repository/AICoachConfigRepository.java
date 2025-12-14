package com.medbot.coach.repository;

import com.medbot.coach.entity.AICoachConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AICoachConfigRepository extends JpaRepository<AICoachConfig, UUID> {
    // Il n'y aura qu'une seule configuration globale
    AICoachConfig findFirstByOrderByCreatedAtDesc();
}

