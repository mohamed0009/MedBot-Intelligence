package com.medbot.user.dto;

import com.medbot.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private User.UserRole role;
    private String formation;
    private User.Niveau niveau;
    private User.UserStatus status;
    private String profilePicture;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

