package com.medbot.course.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.course.entity.Course;
import com.medbot.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "API pour la gestion des cours")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Créer un nouveau cours")
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody Course course) {
        Course created = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Cours créé avec succès", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un cours par son ID")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable UUID id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    @GetMapping
    @Operation(summary = "Liste tous les cours")
    public ResponseEntity<ApiResponse<Page<Course>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UUID formationId,
            @RequestParam(required = false) UUID formateurId,
            @RequestParam(required = false) Course.CourseStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses;

        if (formationId != null) {
            courses = courseService.getCoursesByFormation(formationId, pageable);
        } else if (formateurId != null) {
            courses = courseService.getCoursesByFormateur(formateurId, pageable);
        } else if (status != null) {
            courses = courseService.getCoursesByStatus(status, pageable);
        } else {
            courses = courseService.getAllCourses(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques des cours")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCourseStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", courseService.countByStatus(Course.CourseStatus.APPROUVE) + 
                         courseService.countByStatus(Course.CourseStatus.EN_ATTENTE));
        stats.put("approuves", courseService.countByStatus(Course.CourseStatus.APPROUVE));
        stats.put("enAttente", courseService.countByStatus(Course.CourseStatus.EN_ATTENTE));
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour un cours")
    public ResponseEntity<ApiResponse<Course>> updateCourse(
            @PathVariable UUID id,
            @RequestBody Course course) {
        Course updated = courseService.updateCourse(id, course);
        return ResponseEntity.ok(ApiResponse.success("Cours mis à jour avec succès", updated));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approuver un cours")
    public ResponseEntity<ApiResponse<Course>> approveCourse(@PathVariable UUID id) {
        Course course = courseService.approveCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Cours approuvé avec succès", course));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un cours")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Cours supprimé avec succès", null));
    }
}

