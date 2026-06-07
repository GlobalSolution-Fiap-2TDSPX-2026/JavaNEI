package br.com.fiap.global_solution.dtos;

import br.com.fiap.global_solution.enums.RiskLevel;
import br.com.fiap.global_solution.models.CloseApproach;
import java.time.LocalDate;

public record CloseApproachResponse(
        Long id,
        Long asteroidId,
        String asteroidName,
        Double estimatedDiameterMinKm,
        Double estimatedDiameterMaxKm,
        Double estimatedDiameterAvgKm,
        LocalDate approachDate,
        Double missDistanceKm,
        Double relativeVelocityKmH,
        String orbitingBody,
        RiskLevel riskLevel
) {
    public static CloseApproachResponse fromEntity(CloseApproach c){

        Double min = c.getAsteroid().getEstimatedDiameterMinKm();
        Double max = c.getAsteroid().getEstimatedDiameterMaxKm();
        Double avg = (min != null && max != null) ? (min + max) / 2.0 : null;

        return new CloseApproachResponse(
                c.getId(),
                c.getAsteroid().getId(),
                c.getAsteroid().getName(),
                min,
                max,
                avg,
                c.getApproachDate(),
                c.getMissDistanceKm(),
                c.getRelativeVelocityKmH(),
                c.getOrbitingBody(),
                c.getRiskLevel()

        );
    }

}