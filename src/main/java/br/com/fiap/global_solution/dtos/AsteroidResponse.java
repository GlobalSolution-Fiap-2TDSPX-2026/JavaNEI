package br.com.fiap.global_solution.dtos;

import br.com.fiap.global_solution.models.Asteroid;

public record AsteroidResponse(
        Long id,
        String nasaId,
        String name,
        Boolean isPotentiallyDangerous,
        Double estimatedDiameterMinKm,
        Double estimatedDiameterMaxKm,
        Double estimatedDiameterAvgKm
) {
    public static AsteroidResponse fromEntity(Asteroid a) {
        Double min = a.getEstimatedDiameterMinKm();
        Double max = a.getEstimatedDiameterMaxKm();

        Double avg = (min != null && max != null) ? (min + max) / 2.0 : null;

        return new AsteroidResponse(
                a.getId(),
                a.getNasaId(),
                a.getName(),
                a.getIsPotentiallyDangerous(),
                min,
                max,
                avg
        );
    }
}