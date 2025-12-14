package com.medbot.course.service;

import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.course.entity.Course;
import com.medbot.course.entity.Formation;
import com.medbot.course.repository.CourseRepository;
import com.medbot.course.repository.FormationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final FormationRepository formationRepository;

    @Transactional
    public Course createCourse(Course course) {
        if (course.getFormationId() != null) {
            formationRepository.findById(course.getFormationId())
                .orElseThrow(() -> new ResourceNotFoundException("Formation non trouvée"));
        }
        return courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public Course getCourseById(UUID id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Course> getCoursesByFormation(UUID formationId, Pageable pageable) {
        return courseRepository.findByFormationId(formationId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Course> getCoursesByFormateur(UUID formateurId, Pageable pageable) {
        return courseRepository.findByFormateurId(formateurId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Course> getCoursesByStatus(Course.CourseStatus status, Pageable pageable) {
        return courseRepository.findByStatus(status, pageable);
    }

    @Transactional
    public Course updateCourse(UUID id, Course course) {
        Course existing = getCourseById(id);
        if (course.getTitle() != null) existing.setTitle(course.getTitle());
        if (course.getDescription() != null) existing.setDescription(course.getDescription());
        if (course.getStatus() != null) existing.setStatus(course.getStatus());
        if (course.getNiveau() != null) existing.setNiveau(course.getNiveau());
        if (course.getDureeHeures() != null) existing.setDureeHeures(course.getDureeHeures());
        if (course.getImageUrl() != null) existing.setImageUrl(course.getImageUrl());
        return courseRepository.save(existing);
    }

    @Transactional
    public Course approveCourse(UUID id) {
        Course course = getCourseById(id);
        course.setStatus(Course.CourseStatus.APPROUVE);
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cours non trouvé avec l'ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long countByStatus(Course.CourseStatus status) {
        return courseRepository.countByStatus(status);
    }
}

