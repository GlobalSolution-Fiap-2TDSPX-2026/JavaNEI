package br.com.fiap.global_solution.repositories;

import br.com.fiap.global_solution.models.Asteroid;
import br.com.fiap.global_solution.models.CloseApproach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CloseApproachRepository extends JpaRepository<CloseApproach, Long> {

    Page<CloseApproach> findByAsteroid( Asteroid asteroid, Pageable pageable);

    List<CloseApproach> findByApproachDateBetween(LocalDate start, LocalDate end);

    Page<CloseApproach> findByMissDistanceKmLessThan(Double distance, Pageable pageable);

    List<CloseApproach> findByApproachDate(LocalDate date);

    Optional<CloseApproach> findByAsteroidAndApproachDate(Asteroid asteroid, LocalDate approachDate);



}
