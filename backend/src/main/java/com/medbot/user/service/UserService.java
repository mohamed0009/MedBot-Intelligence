package com.medbot.user.service;

import com.medbot.common.exception.BadRequestException;
import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.user.dto.UserCreateRequest;
import com.medbot.user.dto.UserDTO;
import com.medbot.user.dto.UserUpdateRequest;
import com.medbot.user.entity.User;
import com.medbot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un utilisateur avec cet email existe déjà");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFormation(request.getFormation());
        user.setNiveau(request.getNiveau());
        user.setPhone(request.getPhone());
        user.setStatus(User.UserStatus.ACTIF);

        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        return toDTO(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> searchUsers(String search, Pageable pageable) {
        return userRepository.searchUsers(search, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getUsersByRole(User.UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getUsersByStatus(User.UserStatus status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getUsersByRoleAndStatus(User.UserRole role, User.UserStatus status, Pageable pageable) {
        return userRepository.findByRoleAndStatus(role, status, pageable).map(this::toDTO);
    }

    @Transactional
    public UserDTO updateUser(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Un utilisateur avec cet email existe déjà");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getFormation() != null) user.setFormation(request.getFormation());
        if (request.getNiveau() != null) user.setNiveau(request.getNiveau());
        if (request.getStatus() != null) user.setStatus(request.getStatus());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getProfilePicture() != null) user.setProfilePicture(request.getProfilePicture());

        User updated = userRepository.save(user);
        return toDTO(updated);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDTO activateUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        user.setStatus(User.UserStatus.ACTIF);
        return toDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO deactivateUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
        user.setStatus(User.UserStatus.INACTIF);
        return toDTO(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Long countByRole(User.UserRole role) {
        return userRepository.countByRole(role);
    }

    @Transactional(readOnly = true)
    public Long countByStatus(User.UserStatus status) {
        return userRepository.countByStatus(status);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRole(),
            user.getFormation(),
            user.getNiveau(),
            user.getStatus(),
            user.getProfilePicture(),
            user.getPhone(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}

