package com.medbot.trainer.repository;

import com.medbot.trainer.entity.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {
    Optional<Trainer> findByEmail(String email);
    Page<Trainer> findByStatus(Trainer.TrainerStatus status, Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Trainer t WHERE t.status = :status")
    Long countByStatus(@Param("status") Trainer.TrainerStatus status);
    
    @Query("SELECT AVG(t.rating) FROM Trainer t WHERE t.status = 'ACTIF'")
    Double getAverageRating();
}

