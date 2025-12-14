package com.medbot.qa.repository;

import com.medbot.qa.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    Page<Question> findByPatientId(String patientId, Pageable pageable);
    Page<Question> findByUserId(String userId, Pageable pageable);
    
    @Query("SELECT q FROM Question q WHERE q.patientId = :patientId ORDER BY q.createdAt DESC")
    Page<Question> findByPatientIdOrderByCreatedAtDesc(@Param("patientId") String patientId, Pageable pageable);
}


