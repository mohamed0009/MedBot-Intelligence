package com.medbot.qa.dto;

import com.medbot.qa.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private UUID questionId;
    private String question;
    private String answer;
    private List<Answer.Source> sources;
    private Double confidenceScore;
    private LocalDateTime createdAt;
}


