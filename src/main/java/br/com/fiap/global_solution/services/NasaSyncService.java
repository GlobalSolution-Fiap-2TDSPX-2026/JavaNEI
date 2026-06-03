package br.com.fiap.global_solution.services;

import br.com.fiap.global_solution.dtos.nasa.NasaAsteroid;
import br.com.fiap.global_solution.dtos.nasa.NasaCloseApproachData;
import br.com.fiap.global_solution.dtos.nasa.NasaResponse;
import br.com.fiap.global_solution.models.Asteroid;
import br.com.fiap.global_solution.models.CloseApproach;
import br.com.fiap.global_solution.repositories.AsteroidRepository;
import br.com.fiap.global_solution.repositories.CloseApproachRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class NasaSyncService {

    private final RestTemplate restTemplate;
    private final AsteroidRepository asteroidRepository;
    private final CloseApproachRepository closeApproachRepository;
    private final RiskAssessmentService riskAssessmentService;

    @Value("${nasa.api.key}")
    private String apiKey;

    public NasaSyncService(RestTemplate restTemplate,
                           AsteroidRepository asteroidRepository,
                           CloseApproachRepository closeApproachRepository,
                           RiskAssessmentService riskAssessmentService) {
        this.restTemplate = restTemplate;
        this.asteroidRepository = asteroidRepository;
        this.closeApproachRepository = closeApproachRepository;
        this.riskAssessmentService = riskAssessmentService;
    }


    public int syncAsteroids(LocalDate startDate, LocalDate endDate) {
        String url = buildUrl(startDate, endDate);
        NasaResponse nasaResponse = restTemplate.getForObject(url, NasaResponse.class);

        if (nasaResponse == null || nasaResponse.nearEarthObjects() == null) return 0;

        int count = 0;
        for (var entry : nasaResponse.nearEarthObjects().entrySet()) {
            List<NasaAsteroid> dayAsteroids = entry.getValue();
            if (dayAsteroids == null) continue;

            for (NasaAsteroid nasaDto : dayAsteroids) {
                Asteroid asteroid = asteroidRepository.findByNasaId(nasaDto.id())
                        .orElseGet(() -> {
                            Asteroid a = new Asteroid();
                            a.setNasaId(nasaDto.id());
                            a.setName(nasaDto.name());
                            a.setIsPotentiallyDangerous(nasaDto.isPotentiallyHazardousAsteroid());
                            return asteroidRepository.save(a);
                        });

                if (nasaDto.closeApproachData() != null) {
                    for (NasaCloseApproachData ca : nasaDto.closeApproachData()) {
                        String kmString = ca.missDistance() != null ? ca.missDistance().get("kilometers") : null;
                        if (kmString == null) continue;

                        Double km = Double.parseDouble(kmString);


                        Double velocidadeKmH = null;
                        if (ca.relativeVelocity() != null && ca.relativeVelocity().get("kilometers_per_hour") != null) {
                            velocidadeKmH = Double.parseDouble(ca.relativeVelocity().get("kilometers_per_hour"));
                        }


                        CloseApproach closeApproach = CloseApproach.builder()
                                .asteroid(asteroid)
                                .approachDate(LocalDate.parse(ca.closeApproachDate()))
                                .missDistanceKm(km)
                                .relativeVelocityKmH(velocidadeKmH)
                                .orbitingBody("Earth")

                                .build();
                        closeApproachRepository.save(closeApproach);

                        riskAssessmentService.assessmentImpactRisk(asteroid, km);
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public int syncAsteroidsFromToday() {
        try {
            LocalDate today = LocalDate.now();
            return syncAsteroids(today, today);
        } catch (Exception e) {
            e.printStackTrace(); // veja nos logs do Spring Boot
            throw e;
        }
    }

    private String buildUrl(LocalDate start, LocalDate end) {
        return "https://api.nasa.gov/neo/rest/v1/feed"
                + "?start_date=" + start
                + "&end_date=" + end
                + "&api_key=" + apiKey;
    }
}