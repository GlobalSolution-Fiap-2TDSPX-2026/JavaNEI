package br.com.fiap.global_solution.dtos.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record NasaCloseApproachData(
        @JsonProperty("close_approach_date") String closeApproachDate,
        @JsonProperty("miss_distance") Map<String, String> missDistance,
        @JsonProperty("relative_velocity") Map<String, String> relativeVelocity
) {
}
