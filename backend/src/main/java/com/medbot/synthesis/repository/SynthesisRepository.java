package com.medbot.synthesis.repository;

import com.medbot.synthesis.entity.Synthesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SynthesisRepository extends JpaRepository<Synthesis, UUID> {
    List<Synthesis> findByPatientId(String patientId);
    List<Synthesis> findByPatientIdAndSynthesisType(String patientId, Synthesis.SynthesisType type);
}


