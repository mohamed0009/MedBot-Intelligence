package com.medbot.notification.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.notification.entity.Notification;
import com.medbot.notification.service.NotificationService;
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
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "API pour la gestion des notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle notification")
    public ResponseEntity<ApiResponse<Notification>> createNotification(@RequestBody Notification notification) {
        Notification created = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Notification créée avec succès", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une notification par son ID")
    public ResponseEntity<ApiResponse<Notification>> getNotificationById(@PathVariable UUID id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success(notification));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtenir les notifications d'un utilisateur")
    public ResponseEntity<ApiResponse<Page<Notification>>> getNotificationsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean unreadOnly) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications;

        if (Boolean.TRUE.equals(unreadOnly)) {
            notifications = notificationService.getUnreadNotificationsByUser(userId, pageable);
        } else {
            notifications = notificationService.getNotificationsByUser(userId, pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @GetMapping("/user/{userId}/stats")
    @Operation(summary = "Obtenir les statistiques de notifications d'un utilisateur")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotificationStats(@PathVariable UUID userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("unreadCount", notificationService.countUnreadByUser(userId));
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PostMapping("/{id}/read")
    @Operation(summary = "Marquer une notification comme lue")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(@PathVariable UUID id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marquée comme lue", notification));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une notification")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.success("Notification supprimée avec succès", null));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques globales des notifications")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotificationStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", notificationService.getTotalCount());
        stats.put("scheduled", notificationService.getScheduledCount());
        stats.put("readRate", notificationService.getReadRate());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/scheduled")
    @Operation(summary = "Liste des notifications planifiées")
    public ResponseEntity<ApiResponse<Page<Notification>>> getScheduledNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getScheduledNotifications(pageable);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }
}

