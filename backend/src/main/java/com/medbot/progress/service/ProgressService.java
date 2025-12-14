package com.medbot.progress.service;

import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.progress.entity.Progress;
import com.medbot.progress.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;

    @Transactional
    public Progress createOrUpdateProgress(UUID userId, UUID courseId, Integer modulesCompleted, Integer totalModules, Integer timeSpentMinutes) {
        Progress progress = progressRepository.findByUserIdAndCourseId(userId, courseId)
            .orElse(new Progress());

        progress.setUserId(userId);
        progress.setCourseId(courseId);
        progress.setModulesCompleted(modulesCompleted);
        progress.setTotalModules(totalModules);
        progress.setTimeSpentMinutes(timeSpentMinutes);
        progress.setLastAccessedAt(LocalDateTime.now());

        if (totalModules > 0) {
            double percentage = (modulesCompleted.doubleValue() / totalModules.doubleValue()) * 100.0;
            progress.setCompletionPercentage(Math.min(percentage, 100.0));
        }

        return progressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public Progress getProgressByUserAndCourse(UUID userId, UUID courseId) {
        return progressRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Progression non trouvée"));
    }

    @Transactional(readOnly = true)
    public Page<Progress> getProgressByUser(UUID userId, Pageable pageable) {
        return progressRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Progress> getProgressByCourse(UUID courseId, Pageable pageable) {
        return progressRepository.findByCourseId(courseId, pageable);
    }
}

