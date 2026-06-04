package br.com.fiap.global_solution.models;


import br.com.fiap.global_solution.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_CLOSE_APPROACH")
public class CloseApproach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Asteroid asteroid;

    private LocalDate approachDate;
    private Double missDistanceKm;
    private Double relativeVelocityKmH;
    private String orbitingBody;
    private RiskLevel riskLevel;

}
