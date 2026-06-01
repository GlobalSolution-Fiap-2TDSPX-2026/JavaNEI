package br.com.fiap.global_solution.repositories;

import br.com.fiap.global_solution.enums.AlertLevel;
import br.com.fiap.global_solution.models.RiskAssessment;
import br.com.fiap.global_solution.models.RiskZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskZoneRepository extends JpaRepository<RiskZone,Long> {

    Page<RiskZone> findByAlertLevel(AlertLevel alertLevel, Pageable pageable);

    Page<RiskZone> findByRegionNameContainingIgnoreCase(String region, Pageable pageable);
}
