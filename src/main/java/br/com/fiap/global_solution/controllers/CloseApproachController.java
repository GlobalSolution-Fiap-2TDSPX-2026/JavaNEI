package br.com.fiap.global_solution.controllers;

import br.com.fiap.global_solution.dtos.CloseApproachRequest;
import br.com.fiap.global_solution.dtos.CloseApproachResponse;
import br.com.fiap.global_solution.dtos.CloseApproachesSummaryResponse;
import br.com.fiap.global_solution.models.Asteroid;
import br.com.fiap.global_solution.services.AsteroidService;
import br.com.fiap.global_solution.services.CloseApproachService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/close-approaches")
@Tag(name = "Close Approaches", description = "Aproximações de asteroides à Terra")
public class CloseApproachController {

    private final CloseApproachService closeApproachService;
    private final AsteroidService asteroidService;

    public CloseApproachController(CloseApproachService closeApproachService, AsteroidService asteroidService) {
        this.closeApproachService = closeApproachService;
        this.asteroidService = asteroidService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as aproximações paginadas")
    public ResponseEntity<CloseApproachesSummaryResponse> getAll() {
        CloseApproachesSummaryResponse response = closeApproachService.getSummary();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aproximação por ID")
    public ResponseEntity<CloseApproachResponse> getById(@PathVariable Long id) {
        return closeApproachService.findById(id)
                .map(CloseApproachResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CloseApproach not found"));
    }

    @GetMapping("/asteroid/{asteroidId}")
    @Operation(summary = "Listar aproximações de um asteroide específico")
    public ResponseEntity<Page<CloseApproachResponse>> getByAsteroid(
            @PathVariable Long asteroidId,
            @PageableDefault(size = 10) Pageable pageable) {
        Asteroid asteroid = asteroidService.findById(asteroidId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found"));
        return ResponseEntity.ok(closeApproachService.getCloseApproachesByAsteroid(asteroid, pageable)
                .map(CloseApproachResponse::fromEntity));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Filtrar aproximações por intervalo de datas (formato: yyyy-MM-dd)")
    public ResponseEntity<List<CloseApproachResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<CloseApproachResponse> response = closeApproachService.getCloseApproachesByDate(start, end)
                .stream()
                .map(CloseApproachResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/distance")
    @Operation(summary = "Filtrar aproximações com distância menor que o valor informado (em km)")
    public ResponseEntity<Page<CloseApproachResponse>> getByDistance(
            @RequestParam Double maxDistanceKm,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(closeApproachService.getCloseApproachesByDistanceKmLessThan(maxDistanceKm, pageable)
                .map(CloseApproachResponse::fromEntity));
    }

    @PostMapping
    @Operation(summary = "Registrar uma aproximação manualmente")
    public ResponseEntity<CloseApproachResponse> create(@RequestBody @Valid CloseApproachRequest request) {
        Asteroid asteroid = asteroidService.findById(request.asteroidId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found"));

        var entity = request.toEntity();
        entity.setAsteroid(asteroid);
        var saved = closeApproachService.addCloseApproach(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(CloseApproachResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma aproximação")
    public ResponseEntity<CloseApproachResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid CloseApproachRequest request) {
        Asteroid asteroid = asteroidService.findById(request.asteroidId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found"));

        var entity = request.toEntity();
        entity.setAsteroid(asteroid);
        var updated = closeApproachService.updateCloseApproach(id, entity);
        return ResponseEntity.ok(CloseApproachResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma aproximação")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        closeApproachService.deleteCloseApproach(id);
    }
}