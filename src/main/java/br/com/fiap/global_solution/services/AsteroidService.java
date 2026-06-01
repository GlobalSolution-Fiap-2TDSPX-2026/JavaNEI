package br.com.fiap.global_solution.services;

import br.com.fiap.global_solution.models.Asteroid;
import br.com.fiap.global_solution.repositories.AsteroidRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AsteroidService {

    private final AsteroidRepository asteroidRepository;

    public AsteroidService(AsteroidRepository asteroidRepository) {
        this.asteroidRepository = asteroidRepository;
    }

    public Optional<Asteroid> findById(Long id) {
        return asteroidRepository.findById(id);
    }

    @Cacheable(value = "asteroids")
    public Page<Asteroid> getAsteroids(Pageable pageable) {
        return asteroidRepository.findAll(pageable);
    }

    @Cacheable(value = "nasaId")
    public Optional<Asteroid> findByNasaId(String nasaId, Pageable pageable) {
        return asteroidRepository.findByNasaId(nasaId);
    }

    @Cacheable(value = "names")
    public Page<Asteroid> getAsteroidByName(String name, Pageable pageable) {
        return asteroidRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Cacheable(value = "hazardous")
    public Page<Asteroid> findByPotentiallyHazardous(Boolean isPotentiallyHazardous, Pageable pageable) {
        return asteroidRepository.findByIsPotentiallyDangerous(isPotentiallyHazardous, pageable);
    }


    @CacheEvict(value = {"asteroids", "nasaId", "names", "hazardous"}, allEntries = true)
    public Asteroid addAsteroid(Asteroid asteroid) {
        return asteroidRepository.save(asteroid);
    }

    @CacheEvict(value = {"asteroids", "nasaId", "names", "hazardous"}, allEntries = true)
    public void deleteAsteroid(Long id) {
        var optionalAsteroid = findById(id);
        if (optionalAsteroid.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found");
        asteroidRepository.deleteById(id);
    }

    @CacheEvict(value = {"asteroids", "nasaId", "names", "hazardous"}, allEntries = true)
    public Asteroid updateAsteroid(Long id, Asteroid newAsteroid) {
        var optionalAsteroid = findById(id);
        if (optionalAsteroid.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found");
        newAsteroid.setId(id);
        return asteroidRepository.save(newAsteroid);
    }
}
