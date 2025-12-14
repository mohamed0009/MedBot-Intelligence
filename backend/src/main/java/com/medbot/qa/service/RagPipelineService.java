package com.medbot.qa.service;

import com.medbot.qa.dto.QuestionRequest;
import com.medbot.qa.dto.QuestionResponse;
import com.medbot.qa.entity.Answer;
import com.medbot.qa.entity.Question;
import com.medbot.qa.repository.QuestionRepository;
import com.medbot.search.dto.SearchRequest;
import com.medbot.search.dto.SearchResult;
import com.medbot.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagPipelineService {

    private final SearchService searchService;
    private final LlmService llmService;
    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionResponse askQuestion(QuestionRequest request) {
        // 1. Rechercher les documents pertinents
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(request.getQuestion());
        searchRequest.setTopK(request.getMaxSources());
        searchRequest.setPatientId(request.getPatientId());
        
        List<SearchResult> searchResults = searchService.semanticSearch(searchRequest);

        // 2. Construire le contexte
        String context = buildContext(searchResults);

        // 3. Générer la réponse avec LLM
        String answer = llmService.generateAnswer(request.getQuestion(), context);

        // 4. Sauvegarder la question et la réponse
        Question question = new Question();
        question.setQuestionText(request.getQuestion());
        question.setPatientId(request.getPatientId());
        question.setUserId(request.getUserId());
        question = questionRepository.save(question);

        Answer answerEntity = new Answer();
        answerEntity.setQuestion(question);
        answerEntity.setAnswerText(answer);
        answerEntity.setSources(extractSources(searchResults));
        answerEntity.setConfidenceScore(calculateConfidence(searchResults));
        answerEntity = question.getAnswer() != null ? question.getAnswer() : answerEntity;
        question.setAnswer(answerEntity);
        question = questionRepository.save(question);

        // 5. Construire la réponse
        return new QuestionResponse(
            question.getId(),
            question.getQuestionText(),
            answerEntity.getAnswerText(),
            answerEntity.getSources(),
            answerEntity.getConfidenceScore(),
            question.getCreatedAt()
        );
    }

    private String buildContext(List<SearchResult> results) {
        return results.stream()
            .map(result -> "Document: " + result.getDocumentId() + "\n" + result.getChunkText())
            .collect(Collectors.joining("\n\n---\n\n"));
    }

    private List<Answer.Source> extractSources(List<SearchResult> results) {
        return results.stream()
            .map(result -> new Answer.Source(
                result.getDocumentId(),
                result.getChunkId(),
                result.getChunkText(),
                result.getSimilarityScore()
            ))
            .collect(Collectors.toList());
    }

    private Double calculateConfidence(List<SearchResult> results) {
        if (results.isEmpty()) {
            return 0.0;
        }
        return results.stream()
            .mapToDouble(SearchResult::getSimilarityScore)
            .average()
            .orElse(0.0);
    }
}

