package com.medbot.document.dto;

import com.medbot.document.entity.Document;
import com.medbot.document.entity.DocumentMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private UUID id;
    private String filename;
    private String fileType;
    private Long fileSize;
    private String patientId;
    private String documentType;
    private Document.DocumentStatus status;
    private DocumentMetadata metadata;
    private String author;
    private String department;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


