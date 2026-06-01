package br.com.fiap.global_solution.models;


import br.com.fiap.global_solution.enums.AlertLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_RISK_ZONE")
public class RiskZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String regionName;
    private Double latitude;
    private Double longitude;
    private Double radiusKm;
    private AlertLevel alertLevel;
}
