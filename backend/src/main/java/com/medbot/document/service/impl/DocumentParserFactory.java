package com.medbot.document.service.impl;

import com.medbot.document.service.DocumentParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DocumentParserFactory {

    private final Map<String, DocumentParserService> parsers = new HashMap<>();

    @Autowired
    public DocumentParserFactory(PdfParserService pdfParser, DocxParserService docxParser) {
        parsers.put("pdf", pdfParser);
        parsers.put("docx", docxParser);
        // Add more parsers as needed
    }

    public DocumentParserService getParser(String fileType) {
        DocumentParserService parser = parsers.get(fileType.toLowerCase());
        if (parser == null) {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
        return parser;
    }
}


