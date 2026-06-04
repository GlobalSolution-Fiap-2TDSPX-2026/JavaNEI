package br.com.fiap.global_solution.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_ASTEROID")
public class Asteroid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nasaId;
    private String name;
    private Double estimatedDiameterMinKm;
    private Double estimatedDiameterMaxKm;
    private Double absoluteMagnitude;
    private Boolean isPotentiallyDangerous;

    @OneToMany
    private List<CloseApproach> closeApproaches;

}
