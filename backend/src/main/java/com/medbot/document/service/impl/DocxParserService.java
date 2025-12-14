package com.medbot.document.service.impl;

import com.medbot.document.entity.DocumentMetadata;
import com.medbot.document.service.DocumentParserService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocxParserService implements DocumentParserService {

    @Override
    public ParsedDocument parse(MultipartFile file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            StringBuilder content = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            
            for (XWPFParagraph paragraph : paragraphs) {
                content.append(paragraph.getText()).append("\n");
            }
            
            DocumentMetadata metadata = new DocumentMetadata();
            if (document.getProperties() != null && document.getProperties().getCoreProperties() != null) {
                metadata.setAuthor(document.getProperties().getCoreProperties().getCreator());
                metadata.setTitle(document.getProperties().getCoreProperties().getTitle());
                metadata.setSubject(document.getProperties().getCoreProperties().getSubject());
                if (document.getProperties().getCoreProperties().getCreated() != null) {
                    metadata.setCreationDate(LocalDateTime.ofInstant(
                        document.getProperties().getCoreProperties().getCreated().toInstant(),
                        java.time.ZoneId.systemDefault()));
                }
            }
            
            return new ParsedDocument(content.toString(), metadata);
        }
    }
}


