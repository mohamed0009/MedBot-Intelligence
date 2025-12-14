package com.medbot.document.service;

import com.medbot.document.entity.DocumentMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface DocumentParserService {
    ParsedDocument parse(MultipartFile file) throws IOException;
    
    class ParsedDocument {
        private String content;
        private DocumentMetadata metadata;
        
        public ParsedDocument(String content, DocumentMetadata metadata) {
            this.content = content;
            this.metadata = metadata;
        }
        
        public String getContent() {
            return content;
        }
        
        public DocumentMetadata getMetadata() {
            return metadata;
        }
    }
}


