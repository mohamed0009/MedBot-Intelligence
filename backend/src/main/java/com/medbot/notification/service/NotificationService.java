package com.medbot.notification.service;

import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.notification.entity.Notification;
import com.medbot.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification non trouvée"));
    }

    @Transactional(readOnly = true)
    public Page<Notification> getNotificationsByUser(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Notification> getUnreadNotificationsByUser(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserIdAndIsRead(userId, false, pageable);
    }

    @Transactional
    public Notification markAsRead(UUID id) {
        Notification notification = getNotificationById(id);
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification non trouvée");
        }
        notificationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long countUnreadByUser(UUID userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Long countSent() {
        return notificationRepository.count();
    }

    @Transactional(readOnly = true)
    public Long countScheduled() {
        return notificationRepository.countScheduledReady(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Page<Notification> getScheduledNotifications(Pageable pageable) {
        return notificationRepository.findByScheduledAtIsNotNull(pageable);
    }

    @Transactional(readOnly = true)
    public Double getReadRate() {
        long total = notificationRepository.count();
        if (total == 0) return 0.0;
        long read = notificationRepository.countByIsRead(true);
        return (read * 100.0) / total;
    }

    @Transactional(readOnly = true)
    public Long getTotalCount() {
        return notificationRepository.count();
    }

    @Transactional(readOnly = true)
    public Long getScheduledCount() {
        return notificationRepository.countScheduledReady(LocalDateTime.now());
    }
}

