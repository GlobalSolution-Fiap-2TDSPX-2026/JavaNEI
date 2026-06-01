package br.com.fiap.global_solution.dtos;

import br.com.fiap.global_solution.enums.RiskLevel;
import jakarta.validation.constraints.NotNull;

public record RiskAssessmentRequest(

        @NotNull(message = "asteroidId is required")
        Long asteroidId,

        @NotNull(message = "riskZone is required")
        Long riskZoneId,

        @NotNull(message = "riskLevel is required")
        RiskLevel riskLevel,

        @NotNull(message = "missDistanceKm is required")
        Double missDistanceKm,

        @NotNull(message = "safeDistanceThresholdKm is required")
        Double safeDistanceThresholdKm
) {}