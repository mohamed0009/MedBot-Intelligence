package com.medbot.trainer.service;

import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.trainer.entity.Trainer;
import com.medbot.trainer.repository.TrainerRepository;
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
public class TrainerService {

    private final TrainerRepository trainerRepository;

    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public Trainer getTrainerById(UUID id) {
        return trainerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Formateur non trouvé"));
    }

    @Transactional(readOnly = true)
    public Page<Trainer> getAllTrainers(Pageable pageable) {
        return trainerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Trainer> getTrainersByStatus(Trainer.TrainerStatus status, Pageable pageable) {
        return trainerRepository.findByStatus(status, pageable);
    }

    @Transactional
    public Trainer updateTrainer(UUID id, Trainer trainer) {
        Trainer existing = getTrainerById(id);
        if (trainer.getFirstName() != null) existing.setFirstName(trainer.getFirstName());
        if (trainer.getLastName() != null) existing.setLastName(trainer.getLastName());
        if (trainer.getEmail() != null) existing.setEmail(trainer.getEmail());
        if (trainer.getDescription() != null) existing.setDescription(trainer.getDescription());
        if (trainer.getSkills() != null) existing.setSkills(trainer.getSkills());
        if (trainer.getStatus() != null) existing.setStatus(trainer.getStatus());
        if (trainer.getRating() != null) existing.setRating(trainer.getRating());
        return trainerRepository.save(existing);
    }

    @Transactional
    public Trainer validateTrainer(UUID id) {
        Trainer trainer = getTrainerById(id);
        trainer.setStatus(Trainer.TrainerStatus.ACTIF);
        return trainerRepository.save(trainer);
    }

    @Transactional
    public Trainer suspendTrainer(UUID id) {
        Trainer trainer = getTrainerById(id);
        trainer.setStatus(Trainer.TrainerStatus.SUSPENDU);
        return trainerRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getTrainerStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", trainerRepository.count());
        stats.put("actifs", trainerRepository.countByStatus(Trainer.TrainerStatus.ACTIF));
        stats.put("enAttente", trainerRepository.countByStatus(Trainer.TrainerStatus.EN_ATTENTE));
        stats.put("noteMoyenne", trainerRepository.getAverageRating());
        return stats;
    }
}

