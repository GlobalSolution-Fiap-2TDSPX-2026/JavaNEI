package br.com.fiap.global_solution.dtos.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record NasaAsteroid(
        String id,
        String name,
        @JsonProperty("is_potentially_hazardous_asteroid") boolean isPotentiallyHazardousAsteroid,
        @JsonProperty("close_approach_data") List<NasaCloseApproachData> closeApproachData,
        @JsonProperty("estimated_diameter") Map<String, NasaEstimatedDiameter> estimatedDiameter
) {}