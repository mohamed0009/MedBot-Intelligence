package com.medbot.progress.repository;

import com.medbot.progress.entity.Progress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, UUID> {
    Optional<Progress> findByUserIdAndCourseId(UUID userId, UUID courseId);
    Page<Progress> findByUserId(UUID userId, Pageable pageable);
    Page<Progress> findByCourseId(UUID courseId, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Progress p WHERE p.userId = :userId")
    Long countByUserId(@Param("userId") UUID userId);
}

