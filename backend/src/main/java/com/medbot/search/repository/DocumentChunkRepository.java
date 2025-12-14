package com.medbot.search.repository;

import com.medbot.search.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {
    List<DocumentChunk> findByDocumentId(UUID documentId);
    
    @Query(value = "SELECT * FROM document_chunks WHERE document_id = :documentId ORDER BY chunk_index", 
           nativeQuery = true)
    List<DocumentChunk> findByDocumentIdOrdered(@Param("documentId") UUID documentId);
    
    void deleteByDocumentId(UUID documentId);
}


