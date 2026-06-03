package br.com.fiap.global_solution.controllers;

import br.com.fiap.global_solution.dtos.AsteroidRequest;
import br.com.fiap.global_solution.dtos.AsteroidResponse;
import br.com.fiap.global_solution.services.AsteroidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/asteroids")
@Tag(name = "Asteroids", description = "Gerenciamento de asteroides monitorados")
public class AsteroidController {

    private final AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os asteroides paginados")
    public ResponseEntity<Page<AsteroidResponse>> getAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(asteroidService.getAsteroids(pageable).map(AsteroidResponse::fromEntity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar asteroide por ID interno")
    public ResponseEntity<AsteroidResponse> getById(@PathVariable Long id) {
        return asteroidService.findById(id)
                .map(AsteroidResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found"));
    }

    @GetMapping("/nasa/{nasaId}")
    @Operation(summary = "Buscar asteroide pelo ID da NASA")
    public ResponseEntity<AsteroidResponse> getByNasaId(@PathVariable String nasaId, Pageable pageable) {
        return asteroidService.findByNasaId(nasaId, pageable)
                .map(AsteroidResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asteroid not found"));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar asteroides por nome (parcial, case-insensitive)")
    public ResponseEntity<Page<AsteroidResponse>> getByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(asteroidService.getAsteroidByName(name, pageable).map(AsteroidResponse::fromEntity));
    }

    @GetMapping("/hazardous")
    @Operation(summary = "Filtrar asteroides potencialmente perigosos")
    public ResponseEntity<Page<AsteroidResponse>> getByHazardous(
            @RequestParam Boolean isPotentiallyDangerous,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(
                asteroidService.findByPotentiallyHazardous(isPotentiallyDangerous, pageable)
                        .map(AsteroidResponse::fromEntity));
    }


    @PostMapping
    @Operation(summary = "Cadastrar um asteroide manualmente")
    public ResponseEntity<AsteroidResponse> create(@RequestBody @Valid AsteroidRequest request) {
        var saved = asteroidService.addAsteroid(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AsteroidResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um asteroide")
    public ResponseEntity<AsteroidResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid AsteroidRequest request) {
        var updated = asteroidService.updateAsteroid(id, request.toEntity());
        return ResponseEntity.ok(AsteroidResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um asteroide")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        asteroidService.deleteAsteroid(id);
    }
}