package com.medbot.course.service;

import com.medbot.common.exception.ResourceNotFoundException;
import com.medbot.course.entity.Formation;
import com.medbot.course.repository.FormationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormationService {

    private final FormationRepository formationRepository;

    @Transactional
    public Formation createFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    @Transactional(readOnly = true)
    public Formation getFormationById(UUID id) {
        return formationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Formation non trouvée avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Formation> getAllFormations(Pageable pageable) {
        return formationRepository.findAll(pageable);
    }

    @Transactional
    public Formation updateFormation(UUID id, Formation formation) {
        Formation existing = getFormationById(id);
        if (formation.getTitle() != null) existing.setTitle(formation.getTitle());
        if (formation.getDescription() != null) existing.setDescription(formation.getDescription());
        if (formation.getNiveau() != null) existing.setNiveau(formation.getNiveau());
        if (formation.getDureeTotaleHeures() != null) existing.setDureeTotaleHeures(formation.getDureeTotaleHeures());
        if (formation.getImageUrl() != null) existing.setImageUrl(formation.getImageUrl());
        return formationRepository.save(existing);
    }

    @Transactional
    public void deleteFormation(UUID id) {
        if (!formationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Formation non trouvée avec l'ID: " + id);
        }
        formationRepository.deleteById(id);
    }
}

