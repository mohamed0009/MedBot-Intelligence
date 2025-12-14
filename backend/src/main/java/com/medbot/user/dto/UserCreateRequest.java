package com.medbot.user.dto;

import com.medbot.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank(message = "Le prénom est requis")
    private String firstName;
    
    @NotBlank(message = "Le nom est requis")
    private String lastName;
    
    @NotBlank(message = "L'email est requis")
    @Email(message = "Email invalide")
    private String email;
    
    @NotBlank(message = "Le mot de passe est requis")
    private String password;
    
    @NotNull(message = "Le rôle est requis")
    private User.UserRole role;
    
    private String formation;
    private User.Niveau niveau;
    private String phone;
}

