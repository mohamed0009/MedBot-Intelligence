package com.medbot.synthesis.service;

import com.medbot.document.entity.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimelineService {

    public Map<String, Object> generateTimeline(List<Document> documents) {
        Map<String, Object> timeline = new HashMap<>();
        
        List<Map<String, Object>> events = documents.stream()
            .map(doc -> {
                Map<String, Object> event = new HashMap<>();
                event.put("date", doc.getCreatedAt());
                event.put("type", doc.getDocumentType());
                event.put("title", doc.getFilename());
                event.put("documentId", doc.getId());
                return event;
            })
            .sorted((a, b) -> ((java.time.LocalDateTime) a.get("date"))
                .compareTo((java.time.LocalDateTime) b.get("date")))
            .collect(Collectors.toList());
        
        timeline.put("events", events);
        timeline.put("totalEvents", events.size());
        
        return timeline;
    }
}


