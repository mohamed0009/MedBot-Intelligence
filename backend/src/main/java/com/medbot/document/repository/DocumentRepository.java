package com.medbot.document.repository;

import com.medbot.document.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    
    Page<Document> findByPatientId(String patientId, Pageable pageable);
    
    Page<Document> findByStatus(Document.DocumentStatus status, Pageable pageable);
    
    Page<Document> findByFileType(String fileType, Pageable pageable);
    
    Optional<Document> findByContentHash(String contentHash);
    
    @Query("SELECT d FROM Document d WHERE d.patientId = :patientId AND d.status = :status")
    List<Document> findByPatientIdAndStatus(@Param("patientId") String patientId, 
                                            @Param("status") Document.DocumentStatus status);
    
    @Query("SELECT COUNT(d) FROM Document d WHERE d.patientId = :patientId")
    Long countByPatientId(@Param("patientId") String patientId);
}


