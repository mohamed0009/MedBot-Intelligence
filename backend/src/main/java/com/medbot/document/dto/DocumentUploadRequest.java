package com.medbot.document.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentUploadRequest {
    @NotBlank(message = "File is required")
    private MultipartFile file;
    
    private String patientId;
    private String documentType;
    private String author;
    private String department;
}


