package com.medbot.document.service.impl;

import com.medbot.document.entity.DocumentMetadata;
import com.medbot.document.service.DocumentParserService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PdfParserService implements DocumentParserService {

    @Override
    public ParsedDocument parse(MultipartFile file) throws IOException {
        byte[] pdfBytes = file.getInputStream().readAllBytes();
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String content = stripper.getText(document);
            
            PDDocumentInformation info = document.getDocumentInformation();
            DocumentMetadata metadata = new DocumentMetadata();
            metadata.setAuthor(info.getAuthor());
            metadata.setTitle(info.getTitle());
            metadata.setSubject(info.getSubject());
            metadata.setPageCount(document.getNumberOfPages());
            
            if (info.getCreationDate() != null) {
                metadata.setCreationDate(LocalDateTime.ofInstant(
                    info.getCreationDate().toInstant(), ZoneId.systemDefault()));
            }
            
            return new ParsedDocument(content, metadata);
        }
    }
}

