package br.com.fiap.global_solution.dtos;

import br.com.fiap.global_solution.models.Asteroid;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class AsteroidResponse extends RepresentationModel<AsteroidResponse> {

    private final Long id;
    private final String nasaId;
    private final String name;
    private final Boolean isPotentiallyDangerous;
    private final Double estimatedDiameterMinKm;
    private final Double estimatedDiameterMaxKm;
    private final Double estimatedDiameterAvgKm;

    private AsteroidResponse(Long id, String nasaId, String name, Boolean isPotentiallyDangerous,
                             Double estimatedDiameterMinKm, Double estimatedDiameterMaxKm, Double estimatedDiameterAvgKm) {
        this.id = id;
        this.nasaId = nasaId;
        this.name = name;
        this.isPotentiallyDangerous = isPotentiallyDangerous;
        this.estimatedDiameterMinKm = estimatedDiameterMinKm;
        this.estimatedDiameterMaxKm = estimatedDiameterMaxKm;
        this.estimatedDiameterAvgKm = estimatedDiameterAvgKm;
    }

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