package br.com.fiap.global_solution.repositories;

import br.com.fiap.global_solution.models.Asteroid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AsteroidRepository extends JpaRepository<Asteroid, Long> {

    Optional<Asteroid> findByNasaId(String nasaId);

    Page<Asteroid> findByIsPotentiallyDangerous(Boolean isPotentiallyHazardous, Pageable pageable);

    Page<Asteroid> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
