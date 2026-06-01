package br.com.fiap.global_solution.dtos;

import br.com.fiap.global_solution.models.RiskAssessment;
import br.com.fiap.global_solution.enums.RiskLevel;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RiskAssessmentResponse(
        Long id,
        String asteroidName,
        String riskZoneName,
        RiskLevel riskLevel,
        Double missDistanceKm,
        Double safeDistanceThresholdKm,
        LocalDateTime assessedAt
) {
    public static RiskAssessmentResponse fromEntity(RiskAssessment rA){
        return new RiskAssessmentResponse(
                rA.getId(),
                rA.getAsteroid().getName(),
                rA.getRiskZone().getRegionName(),
                rA.getRiskLevel(),
                rA.getMissDistanceKm(),
                rA.getSafeDistanceThresholdKm(),
                rA.getAssessedAt()
        );
    }
    }
