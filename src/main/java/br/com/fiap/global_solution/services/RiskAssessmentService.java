package br.com.fiap.global_solution.services;

import br.com.fiap.global_solution.enums.AlertLevel;
import br.com.fiap.global_solution.enums.RiskLevel;
import br.com.fiap.global_solution.models.Asteroid;
import br.com.fiap.global_solution.models.RiskAssessment;
import br.com.fiap.global_solution.models.RiskZone;
import br.com.fiap.global_solution.repositories.RiskAssessmentRepository;
import br.com.fiap.global_solution.repositories.RiskZoneRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RiskAssessmentService {

    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RiskZoneRepository riskZoneRepository;
    private final RiskZoneService riskZoneService;

    public RiskAssessmentService(RiskAssessmentRepository riskAssessmentRepository, RiskZoneRepository riskZoneRepository,  RiskZoneService riskZoneService) {
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.riskZoneRepository = riskZoneRepository;
        this.riskZoneService = riskZoneService;
    }

    public Optional<RiskAssessment> findById(Long id) {
        return riskAssessmentRepository.findById(id);
    }

    @Cacheable(value = "riskAssessment")
    public Page<RiskAssessment> getRiskAssessments(Pageable pageable) {
        return riskAssessmentRepository.findAll(pageable);
    }

    @Cacheable(value = "riskLevel")
    public Page<RiskAssessment> getByRiskLevel(RiskLevel riskLevel, Pageable pageable) {
        return riskAssessmentRepository.findByRiskLevel(riskLevel, pageable);
    }

    @Cacheable(value = "risksLevel")
    public Page<RiskAssessment> getByListOfRiskLevels(List<RiskLevel> riskLevels, Pageable pageable) {
        return riskAssessmentRepository.findByRiskLevelIn(riskLevels, pageable);
    }

    @Cacheable(value = "assessmentDate")
    public Page<RiskAssessment> getByAssessedAt(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return riskAssessmentRepository.findByAssessedAtBetween(start, end, pageable);
    }

    public Page<RiskAssessment> findByRiskZoneId(Long riskZoneId, Pageable pageable) {
        return riskAssessmentRepository.findByRiskZoneId(riskZoneId, pageable);
    }

    @CacheEvict(value = {"riskAssessment", "riskLevel", "risksLevel", "assessmentDate"}, allEntries = true)
    public RiskAssessment addRiskAssessment(RiskAssessment riskAssessment) {
        return riskAssessmentRepository.save(riskAssessment);
    }

    @CacheEvict(value = {"riskAssessment", "riskLevel", "risksLevel", "assessmentDate"}, allEntries = true)
    public void deleteRiskAssessment(Long id) {
        var optionalRiskAssessment = riskAssessmentRepository.findById(id);
        if (optionalRiskAssessment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskAssessment not found");
        }
        riskAssessmentRepository.deleteById(id);
    }

    @CacheEvict(value = {"riskAssessment", "riskLevel", "risksLevel", "assessmentDate"}, allEntries = true)
    public RiskAssessment updateRiskAssessment(Long id, RiskAssessment newRiskAssessment) {
        var optionalRiskAssessment = riskAssessmentRepository.findById(id);
        if (optionalRiskAssessment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskAssessment not found");
        }
        newRiskAssessment.setId(id);
        return riskAssessmentRepository.save(newRiskAssessment);
    }


    @CacheEvict(value = {"riskAssessment", "riskLevel", "risksLevel", "assessmentDate"}, allEntries = true)
    public void assessmentImpactRisk(Asteroid asteroid, Double distanceKm) {
        RiskLevel riskLevel = calculateRiskLevel(asteroid, distanceKm);
        AlertLevel alertLevel = toAlertLevel(riskLevel);

        List<RiskZone> zones = riskZoneRepository.findAll();
        for (RiskZone zone : zones) {
            if (alertLevel.ordinal() > (zone.getAlertLevel() == null ? 0 : zone.getAlertLevel().ordinal())) {
                zone.setAlertLevel(alertLevel);
                riskZoneService.addRiskZone(zone);
            }

            RiskAssessment assessment = RiskAssessment.builder()
                    .asteroid(asteroid)
                    .riskZone(zone)
                    .riskLevel(riskLevel)
                    .missDistanceKm(distanceKm)
                    .safeDistanceThresholdKm(7_500_000.0)
                    .assessedAt(LocalDateTime.now())
                    .build();

            riskAssessmentRepository.save(assessment);
        }
    }

    private RiskLevel calculateRiskLevel(Asteroid asteroid, Double distanceKm) {
        if (Boolean.TRUE.equals(asteroid.getIsPotentiallyDangerous())) {
            if (distanceKm <= 500_000.0)   return RiskLevel.CRITICAL;
            if (distanceKm <= 2_000_000.0) return RiskLevel.HIGH;
            if (distanceKm <= 7_500_000.0) return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private AlertLevel toAlertLevel(RiskLevel riskLevel) {
        return switch (riskLevel) {
            case CRITICAL -> AlertLevel.RED;
            case HIGH     -> AlertLevel.ORANGE;
            case MEDIUM   -> AlertLevel.YELLOW;
            default       -> AlertLevel.GREEN;
        };
    }
}