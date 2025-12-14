package com.medbot.document.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentMetadata {
    private String author;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private Integer pageCount;
    private String title;
    private String subject;
    private Map<String, Object> customFields;
}


