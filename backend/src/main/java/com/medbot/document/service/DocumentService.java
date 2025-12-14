package com.medbot.document.service;

import com.medbot.common.exception.BadRequestException;
import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.document.dto.DocumentDTO;
import com.medbot.document.dto.DocumentUploadRequest;
import com.medbot.document.entity.Document;
import com.medbot.document.entity.DocumentMetadata;
import com.medbot.document.repository.DocumentRepository;
import com.medbot.document.service.impl.DocumentParserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

import static com.medbot.common.config.RabbitMQConfig.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final DocumentParserFactory parserFactory;
    private final RabbitTemplate rabbitTemplate;

    @Value("${medbot.document.max-file-size:104857600}")
    private long maxFileSize;

    @Transactional
    public DocumentDTO uploadDocument(DocumentUploadRequest request) {
        MultipartFile file = request.getFile();
        
        // Validate file
        validateFile(file);
        
        // Extract file type
        String fileType = extractFileType(file.getOriginalFilename());
        
        // Create document entity
        Document document = new Document();
        document.setFilename(file.getOriginalFilename());
        document.setFileType(fileType);
        document.setFileSize(file.getSize());
        document.setPatientId(request.getPatientId());
        document.setDocumentType(request.getDocumentType());
        document.setAuthor(request.getAuthor());
        document.setDepartment(request.getDepartment());
        document.setStatus(Document.DocumentStatus.PROCESSING);
        
        // Generate ID and store file
        UUID documentId = UUID.randomUUID();
        document.setId(documentId);
        
        try {
            String filePath = fileStorageService.storeFile(file, documentId);
            document.setFilePath(filePath);
            
            // Parse document
            DocumentParserService parser = parserFactory.getParser(fileType);
            DocumentParserService.ParsedDocument parsed = parser.parse(file);
            document.setContent(parsed.getContent());
            document.setMetadata(parsed.getMetadata());
            
            // Calculate content hash
            String hash = calculateHash(parsed.getContent());
            document.setContentHash(hash);
            
            // Check for duplicates
            documentRepository.findByContentHash(hash).ifPresent(existing -> {
                throw new BadRequestException("Document already exists with same content");
            });
            
            document.setStatus(Document.DocumentStatus.PROCESSED);
            Document saved = documentRepository.save(document);
            
            // Publish to RabbitMQ
            publishDocumentProcessed(saved);
            
            return toDTO(saved);
            
        } catch (Exception e) {
            log.error("Error processing document", e);
            document.setStatus(Document.DocumentStatus.FAILED);
            documentRepository.save(document);
            throw new BadRequestException("Failed to process document: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public DocumentDTO getDocumentById(UUID id) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        return toDTO(document);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> getAllDocuments(Pageable pageable) {
        return documentRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> getDocumentsByPatientId(String patientId, Pageable pageable) {
        return documentRepository.findByPatientId(patientId, pageable).map(this::toDTO);
    }

    @Transactional
    public DocumentDTO updateDocument(UUID id, DocumentMetadata metadata, String documentType) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        
        if (metadata != null) {
            document.setMetadata(metadata);
        }
        if (documentType != null) {
            document.setDocumentType(documentType);
        }
        
        return toDTO(documentRepository.save(document));
    }

    @Transactional
    public void deleteDocument(UUID id) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        
        try {
            fileStorageService.deleteFile(document.getFilePath());
        } catch (Exception e) {
            log.warn("Failed to delete file: " + document.getFilePath(), e);
        }
        
        documentRepository.delete(document);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        if (file.getSize() > maxFileSize) {
            throw new BadRequestException("File size exceeds maximum allowed size");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !isValidFileType(filename)) {
            throw new BadRequestException("Invalid file type");
        }
    }

    private boolean isValidFileType(String filename) {
        String extension = extractFileType(filename);
        return extension.equals("pdf") || extension.equals("docx") || 
               extension.equals("txt") || extension.equals("hl7") || 
               extension.equals("json") || extension.equals("xml");
    }

    private String extractFileType(String filename) {
        if (filename == null) return "unknown";
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1).toLowerCase() : "unknown";
    }

    private String calculateHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hash", e);
        }
    }

    private void publishDocumentProcessed(Document document) {
        try {
            rabbitTemplate.convertAndSend(
                DOCUMENT_EXCHANGE,
                DOCUMENT_PROCESSED_ROUTING_KEY,
                document.getId().toString()
            );
            log.info("Published document processed event for document: {}", document.getId());
        } catch (Exception e) {
            log.error("Failed to publish document processed event", e);
        }
    }

    private DocumentDTO toDTO(Document document) {
        return new DocumentDTO(
            document.getId(),
            document.getFilename(),
            document.getFileType(),
            document.getFileSize(),
            document.getPatientId(),
            document.getDocumentType(),
            document.getStatus(),
            document.getMetadata(),
            document.getAuthor(),
            document.getDepartment(),
            document.getCreatedAt(),
            document.getUpdatedAt()
        );
    }
}


