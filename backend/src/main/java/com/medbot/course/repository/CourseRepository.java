package com.medbot.course.repository;

import com.medbot.course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Page<Course> findByFormationId(UUID formationId, Pageable pageable);
    Page<Course> findByFormateurId(UUID formateurId, Pageable pageable);
    Page<Course> findByStatus(Course.CourseStatus status, Pageable pageable);
    Long countByStatus(Course.CourseStatus status);
}

