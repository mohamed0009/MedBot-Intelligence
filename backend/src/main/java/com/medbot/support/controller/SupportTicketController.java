package com.medbot.support.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.support.entity.SupportTicket;
import com.medbot.support.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/support/tickets")
@RequiredArgsConstructor
@Tag(name = "Support Tickets", description = "API pour la gestion des tickets de support")
public class SupportTicketController {

    private final SupportTicketService ticketService;

    @PostMapping
    @Operation(summary = "Créer un nouveau ticket de support")
    public ResponseEntity<ApiResponse<SupportTicket>> createTicket(@RequestBody SupportTicket ticket) {
        SupportTicket created = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Ticket créé avec succès", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un ticket par son ID")
    public ResponseEntity<ApiResponse<SupportTicket>> getTicketById(@PathVariable UUID id) {
        SupportTicket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ApiResponse.success(ticket));
    }

    @GetMapping
    @Operation(summary = "Liste tous les tickets avec filtres")
    public ResponseEntity<ApiResponse<Page<SupportTicket>>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) SupportTicket.TicketStatus status,
            @RequestParam(required = false) SupportTicket.Category category,
            @RequestParam(required = false) SupportTicket.Priority priority) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupportTicket> tickets;

        if (userId != null) {
            tickets = ticketService.getTicketsByUser(userId, pageable);
        } else if (status != null) {
            tickets = ticketService.getTicketsByStatus(status, pageable);
        } else if (category != null) {
            tickets = ticketService.getTicketsByCategory(category, pageable);
        } else if (priority != null) {
            tickets = ticketService.getTicketsByPriority(priority, pageable);
        } else {
            tickets = ticketService.getAllTickets(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(tickets));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques des tickets")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getTicketStats() {
        Map<String, Long> stats = ticketService.getTicketStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Mettre à jour le statut d'un ticket")
    public ResponseEntity<ApiResponse<SupportTicket>> updateTicketStatus(
            @PathVariable UUID id,
            @RequestParam SupportTicket.TicketStatus status) {
        SupportTicket ticket = ticketService.updateTicketStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Statut mis à jour", ticket));
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assigner un ticket à un utilisateur")
    public ResponseEntity<ApiResponse<SupportTicket>> assignTicket(
            @PathVariable UUID id,
            @RequestParam UUID assignedTo) {
        SupportTicket ticket = ticketService.assignTicket(id, assignedTo);
        return ResponseEntity.ok(ApiResponse.success("Ticket assigné", ticket));
    }
}

