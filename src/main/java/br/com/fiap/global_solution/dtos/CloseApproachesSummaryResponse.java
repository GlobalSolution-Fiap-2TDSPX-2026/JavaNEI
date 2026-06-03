package br.com.fiap.global_solution.dtos;

import br.com.fiap.global_solution.enums.RiskLevel;
import java.util.List;

public record CloseApproachesSummaryResponse(
        int countAsteroids,
        Double minDistanceKm,
        String minDistanceAsteroid,
        RiskLevel highestRisk,
        List<CloseApproachResponse> approaches
) {}