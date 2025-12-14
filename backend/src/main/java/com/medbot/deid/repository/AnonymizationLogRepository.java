package com.medbot.deid.repository;

import com.medbot.deid.entity.AnonymizationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnonymizationLogRepository extends JpaRepository<AnonymizationLog, UUID> {
    List<AnonymizationLog> findByDocumentId(UUID documentId);
}


