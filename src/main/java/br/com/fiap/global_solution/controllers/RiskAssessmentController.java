package br.com.fiap.global_solution.controllers;

import br.com.fiap.global_solution.dtos.RiskAssessmentRequest;
import br.com.fiap.global_solution.dtos.RiskAssessmentResponse;
import br.com.fiap.global_solution.enums.RiskLevel;
import br.com.fiap.global_solution.models.Asteroid;
import br.com.fiap.global_solution.models.RiskAssessment;
import br.com.fiap.global_solution.models.RiskZone;
import br.com.fiap.global_solution.services.AsteroidService;
import br.com.fiap.global_solution.services.RiskAssessmentService;
import br.com.fiap.global_solution.services.RiskZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/risk-assessments")
@Tag(name = "Risk Assessments", description = "Avaliações de risco geradas para cada asteroide × região")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;
    private final AsteroidService asteroidService;
    private final RiskZoneService riskZoneService;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService,
                                    AsteroidService asteroidService,
                                    RiskZoneService riskZoneService) {
        this.riskAssessmentService = riskAssessmentService;
        this.asteroidService = asteroidService;
        this.riskZoneService = riskZoneService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as avaliações de risco")
    public ResponseEntity<Page<RiskAssessmentResponse>> getAll(
            @PageableDefault(size = 10, sort = "assessedAt") Pageable pageable) {
        return ResponseEntity.ok(riskAssessmentService.getRiskAssessments(pageable)
                .map(RiskAssessmentResponse::fromEntity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar avaliação de risco por ID")
    public ResponseEntity<RiskAssessmentResponse> getById(@PathVariable Long id) {
        return riskAssessmentService.findById(id)
                .map(RiskAssessmentResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskAssessment not found"));
    }

    @GetMapping("/level/{riskLevel}")
    @Operation(summary = "Filtrar avaliações por nível de risco (LOW, MEDIUM, HIGH, CRITICAL)")
    public ResponseEntity<Page<RiskAssessmentResponse>> getByLevel(
            @PathVariable RiskLevel riskLevel,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(riskAssessmentService.getByRiskLevel(riskLevel, pageable)
                .map(RiskAssessmentResponse::fromEntity));
    }

    @GetMapping("/levels")
    @Operation(summary = "Filtrar avaliações por múltiplos níveis de risco")
    public ResponseEntity<Page<RiskAssessmentResponse>> getByLevels(
            @RequestParam List<RiskLevel> riskLevels,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(riskAssessmentService.getByListOfRiskLevels(riskLevels, pageable)
                .map(RiskAssessmentResponse::fromEntity));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Filtrar avaliações por intervalo de datas/hora (ISO-8601)")
    public ResponseEntity<Page<RiskAssessmentResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(riskAssessmentService.getByAssessedAt(start, end, pageable)
                .map(RiskAssessmentResponse::fromEntity));
    }

    @GetMapping("/zone/{riskZoneId}")
    @Operation(summary = "Listar avaliações de risco de uma zona/região específica")
    public ResponseEntity<Page<RiskAssessmentResponse>> getByZone(
            @PathVariable Long riskZoneId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(riskAssessmentService.findByRiskZoneId(riskZoneId, pageable)
                .map(RiskAssessmentResponse::fromEntity));
    }

    @PostMapping
    @Operation(summary = "Criar uma avaliação de risco manualmente")
    public ResponseEntity<RiskAssessmentResponse> create(@RequestBody @Valid RiskAssessmentRequest request) {
        Asteroid asteroid = asteroidService.findById(request.asteroidId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found"));
        RiskZone riskZone = riskZoneService.findById(request.riskZoneId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskZone not found"));

        RiskAssessment assessment = RiskAssessment.builder()
                .asteroid(asteroid)
                .riskZone(riskZone)
                .riskLevel(request.riskLevel())
                .missDistanceKm(request.missDistanceKm())
                .safeDistanceThresholdKm(request.safeDistanceThresholdKm())
                .assessedAt(LocalDateTime.now())
                .build();

        var saved = riskAssessmentService.addRiskAssessment(assessment);
        return ResponseEntity.status(HttpStatus.CREATED).body(RiskAssessmentResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma avaliação de risco")
    public ResponseEntity<RiskAssessmentResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid RiskAssessmentRequest request) {
        Asteroid asteroid = asteroidService.findById(request.asteroidId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found"));
        RiskZone riskZone = riskZoneService.findById(request.riskZoneId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskZone not found"));

        RiskAssessment assessment = RiskAssessment.builder()
                .asteroid(asteroid)
                .riskZone(riskZone)
                .riskLevel(request.riskLevel())
                .missDistanceKm(request.missDistanceKm())
                .safeDistanceThresholdKm(request.safeDistanceThresholdKm())
                .assessedAt(LocalDateTime.now())
                .build();

        var updated = riskAssessmentService.updateRiskAssessment(id, assessment);
        return ResponseEntity.ok(RiskAssessmentResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma avaliação de risco")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        riskAssessmentService.deleteRiskAssessment(id);
    }
}