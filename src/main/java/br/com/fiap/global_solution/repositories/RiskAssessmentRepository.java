package br.com.fiap.global_solution.repositories;

import br.com.fiap.global_solution.enums.RiskLevel;
import br.com.fiap.global_solution.models.Asteroid;
import br.com.fiap.global_solution.models.RiskAssessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
    Optional<RiskAssessment> findByAsteroid(Asteroid asteroid);

    Page<RiskAssessment> findByRiskLevel(RiskLevel riskLevel, Pageable pageable);

    Page<RiskAssessment> findByRiskLevelIn(List<RiskLevel> levels, Pageable pageable);

    Page<RiskAssessment> findByAssessedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<RiskAssessment> findByRiskZoneId(Long riskZoneId, Pageable pageable);

}
