package com.medbot.support.service;

import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.support.entity.SupportTicket;
import com.medbot.support.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository ticketRepository;

    @Transactional
    public SupportTicket createTicket(SupportTicket ticket) {
        // Générer le numéro de ticket
        Integer maxNumber = ticketRepository.findMaxTicketNumber();
        int nextNumber = (maxNumber != null ? maxNumber : 0) + 1;
        ticket.setTicketNumber(String.format("TICKET-%03d", nextNumber));
        
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public SupportTicket getTicketById(UUID id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé"));
    }

    @Transactional(readOnly = true)
    public Page<SupportTicket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<SupportTicket> getTicketsByUser(UUID userId, Pageable pageable) {
        return ticketRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SupportTicket> getTicketsByStatus(SupportTicket.TicketStatus status, Pageable pageable) {
        return ticketRepository.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SupportTicket> getTicketsByCategory(SupportTicket.Category category, Pageable pageable) {
        return ticketRepository.findByCategory(category, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SupportTicket> getTicketsByPriority(SupportTicket.Priority priority, Pageable pageable) {
        return ticketRepository.findByPriority(priority, pageable);
    }

    @Transactional
    public SupportTicket updateTicketStatus(UUID id, SupportTicket.TicketStatus status) {
        SupportTicket ticket = getTicketById(id);
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public SupportTicket assignTicket(UUID id, UUID assignedTo) {
        SupportTicket ticket = getTicketById(id);
        ticket.setAssignedTo(assignedTo);
        ticket.setStatus(SupportTicket.TicketStatus.EN_COURS);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getTicketStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", ticketRepository.count());
        stats.put("ouverts", ticketRepository.countByStatus(SupportTicket.TicketStatus.OUVERT));
        stats.put("enCours", ticketRepository.countByStatus(SupportTicket.TicketStatus.EN_COURS));
        stats.put("resolus", ticketRepository.countByStatus(SupportTicket.TicketStatus.RESOLU));
        return stats;
    }
}

