package com.medbot.user.controller;

import com.medbot.common.dto.ApiResponse;
import com.medbot.user.dto.UserCreateRequest;
import com.medbot.user.dto.UserDTO;
import com.medbot.user.dto.UserUpdateRequest;
import com.medbot.user.entity.User;
import com.medbot.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Créer un nouvel utilisateur")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Utilisateur créé avec succès", user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur par son ID")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping
    @Operation(summary = "Liste tous les utilisateurs avec pagination")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) User.UserRole role,
            @RequestParam(required = false) User.UserStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users;

        if (search != null && !search.isEmpty()) {
            users = userService.searchUsers(search, pageable);
        } else if (role != null && status != null) {
            users = userService.getUsersByRoleAndStatus(role, status, pageable);
        } else if (role != null) {
            users = userService.getUsersByRole(role, pageable);
        } else if (status != null) {
            users = userService.getUsersByStatus(status, pageable);
        } else {
            users = userService.getAllUsers(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les statistiques des utilisateurs")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", userService.countByStatus(User.UserStatus.ACTIF) + 
                         userService.countByStatus(User.UserStatus.INACTIF));
        stats.put("actifs", userService.countByStatus(User.UserStatus.ACTIF));
        stats.put("apprenants", userService.countByRole(User.UserRole.APPRENANT));
        stats.put("formateurs", userService.countByRole(User.UserRole.FORMATEUR));
        stats.put("administrateurs", userService.countByRole(User.UserRole.ADMINISTRATEUR));
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur mis à jour avec succès", user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur supprimé avec succès", null));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activer un utilisateur")
    public ResponseEntity<ApiResponse<UserDTO>> activateUser(@PathVariable UUID id) {
        UserDTO user = userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur activé avec succès", user));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver un utilisateur")
    public ResponseEntity<ApiResponse<UserDTO>> deactivateUser(@PathVariable UUID id) {
        UserDTO user = userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur désactivé avec succès", user));
    }
}

