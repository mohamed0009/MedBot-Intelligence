package com.medbot.support.repository;

import com.medbot.support.entity.SupportTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
    Optional<SupportTicket> findByTicketNumber(String ticketNumber);
    Page<SupportTicket> findByUserId(UUID userId, Pageable pageable);
    Page<SupportTicket> findByStatus(SupportTicket.TicketStatus status, Pageable pageable);
    Page<SupportTicket> findByCategory(SupportTicket.Category category, Pageable pageable);
    Page<SupportTicket> findByPriority(SupportTicket.Priority priority, Pageable pageable);
    Page<SupportTicket> findByAssignedTo(UUID assignedTo, Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.status = :status")
    Long countByStatus(@Param("status") SupportTicket.TicketStatus status);
    
    @Query("SELECT MAX(CAST(SUBSTRING(t.ticketNumber, 8) AS int)) FROM SupportTicket t WHERE t.ticketNumber LIKE 'TICKET-%'")
    Integer findMaxTicketNumber();
}

