package br.com.fiap.global_solution.models;


import br.com.fiap.global_solution.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_RISK_ASSESSMENT")
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Asteroid asteroid;

    @ManyToOne
    @JoinColumn(name = "risk_zone_id")
    private RiskZone riskZone;

    private RiskLevel riskLevel;
    private Double missDistanceKm;
    private Double safeDistanceThresholdKm;
    private LocalDateTime assessedAt;
}
