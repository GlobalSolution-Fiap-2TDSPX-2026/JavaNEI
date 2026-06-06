package br.com.fiap.global_solution.dtos.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NasaEstimatedDiameter(
        @JsonProperty("estimated_diameter_min") Double estimatedDiameterMin,
        @JsonProperty("estimated_diameter_max") Double estimatedDiameterMax
) {}