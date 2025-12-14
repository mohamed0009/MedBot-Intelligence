package com.medbot.common.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.course.service.CourseService;
import com.medbot.course.entity.Course;
import com.medbot.notification.service.NotificationService;
import com.medbot.support.service.SupportTicketService;
import com.medbot.trainer.service.TrainerService;
import com.medbot.user.service.UserService;
import com.medbot.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "API pour le tableau de bord administrateur")
public class DashboardController {

    private final UserService userService;
    private final CourseService courseService;
    private final TrainerService trainerService;
    private final NotificationService notificationService;
    private final SupportTicketService ticketService;

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques globales du tableau de bord")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Statistiques utilisateurs
        Map<String, Object> users = new HashMap<>();
        users.put("total", userService.countByStatus(User.UserStatus.ACTIF) + 
                          userService.countByStatus(User.UserStatus.INACTIF));
        users.put("actifs", userService.countByStatus(User.UserStatus.ACTIF));
        users.put("apprenants", userService.countByRole(User.UserRole.APPRENANT));
        users.put("formateurs", userService.countByRole(User.UserRole.FORMATEUR));
        stats.put("users", users);
        
        // Statistiques cours
        Map<String, Object> courses = new HashMap<>();
        courses.put("total", courseService.countByStatus(Course.CourseStatus.APPROUVE) + 
                            courseService.countByStatus(Course.CourseStatus.EN_ATTENTE));
        courses.put("approuves", courseService.countByStatus(Course.CourseStatus.APPROUVE));
        courses.put("enAttente", courseService.countByStatus(Course.CourseStatus.EN_ATTENTE));
        stats.put("courses", courses);
        
        // Statistiques formateurs
        Map<String, Object> trainers = trainerService.getTrainerStats();
        stats.put("trainers", trainers);
        
        // Statistiques notifications
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("total", notificationService.getTotalCount());
        notifications.put("scheduled", notificationService.getScheduledCount());
        stats.put("notifications", notifications);
        
        // Statistiques tickets
        Map<String, Long> tickets = ticketService.getTicketStats();
        stats.put("tickets", tickets);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}

