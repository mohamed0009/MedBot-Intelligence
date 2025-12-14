package com.medbot.notification.repository;

import com.medbot.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByUserId(UUID userId, Pageable pageable);
    Page<Notification> findByUserIdAndIsRead(UUID userId, Boolean isRead, Pageable pageable);
    Page<Notification> findByType(Notification.NotificationType type, Pageable pageable);
    Page<Notification> findByTargetAudience(Notification.TargetAudience audience, Pageable pageable);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.isRead = false AND (n.userId = :userId OR n.userId IS NULL)")
    Long countUnreadByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.scheduledAt <= :now AND n.scheduledAt IS NOT NULL")
    Long countScheduledReady(@Param("now") LocalDateTime now);
    
    Page<Notification> findByScheduledAtIsNotNull(Pageable pageable);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.isRead = :isRead")
    Long countByIsRead(@Param("isRead") Boolean isRead);
}

