package br.com.fiap.global_solution.dtos;

import br.com.fiap.global_solution.enums.RiskLevel;
import br.com.fiap.global_solution.models.CloseApproach;
import java.time.LocalDate;

public record CloseApproachResponse(
        Long id,
        String asteroidName,
        LocalDate approachDate,
        Double missDistanceKm,
        Double relativeVelocityKmH,
        String orbitingBody,
        RiskLevel riskLevel
) {
    public static CloseApproachResponse fromEntity(CloseApproach c){
        return new CloseApproachResponse(
                c.getId(),
                c.getAsteroid().getName(),
                c.getApproachDate(),
                c.getMissDistanceKm(),
                c.getRelativeVelocityKmH(),
                c.getOrbitingBody(),
                c.getRiskLevel()

        );
    }

}