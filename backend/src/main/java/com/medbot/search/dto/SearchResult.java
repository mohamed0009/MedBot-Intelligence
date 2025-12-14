package com.medbot.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
    private UUID chunkId;
    private UUID documentId;
    private String chunkText;
    private Double similarityScore;
    private Integer chunkIndex;
    private String documentTitle;
}


