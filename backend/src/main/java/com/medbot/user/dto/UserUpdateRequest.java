package com.medbot.user.dto;

import com.medbot.user.entity.User;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String formation;
    private User.Niveau niveau;
    private User.UserStatus status;
    private String phone;
    private String profilePicture;
}

