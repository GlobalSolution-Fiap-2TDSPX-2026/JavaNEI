package br.com.fiap.global_solution.controllers;

import br.com.fiap.global_solution.dtos.RiskZoneRequest;
import br.com.fiap.global_solution.dtos.RiskZoneResponse;
import br.com.fiap.global_solution.enums.AlertLevel;
import br.com.fiap.global_solution.services.RiskZoneService;
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
@RequestMapping("/risk-zones")
@Tag(name = "Risk Zones", description = "Regiões monitoradas com nível de alerta por proximidade de asteroides")
public class RiskZoneController {

    private final RiskZoneService riskZoneService;

    public RiskZoneController(RiskZoneService riskZoneService) {
        this.riskZoneService = riskZoneService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as zonas de risco")
    public ResponseEntity<Page<RiskZoneResponse>> getAll(
            @PageableDefault(size = 10, sort = "regionName") Pageable pageable) {
        return ResponseEntity.ok(riskZoneService.getRiskZones(pageable).map(RiskZoneResponse::fromEntity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar zona de risco por ID")
    public ResponseEntity<RiskZoneResponse> getById(@PathVariable Long id) {
        return riskZoneService.findById(id)
                .map(RiskZoneResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskZone not found"));
    }

    @GetMapping("/alert")
    @Operation(summary = "Filtrar zonas por nível de alerta (GREEN, YELLOW, ORANGE, RED)")
    public ResponseEntity<Page<RiskZoneResponse>> getByAlertLevel(
            @RequestParam AlertLevel alertLevel,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(riskZoneService.getZoneByAlertLevel(alertLevel, pageable)
                .map(RiskZoneResponse::fromEntity));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar zonas por nome da região (parcial, case-insensitive)")
    public ResponseEntity<Page<RiskZoneResponse>> getByRegion(
            @RequestParam String region,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(riskZoneService.getZoneByRegionName(region, pageable)
                .map(RiskZoneResponse::fromEntity));
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova zona de risco/região monitorada")
    public ResponseEntity<RiskZoneResponse> create(@RequestBody @Valid RiskZoneRequest request) {
        var saved = riskZoneService.addRiskZone(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(RiskZoneResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma zona de risco")
    public ResponseEntity<RiskZoneResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid RiskZoneRequest request) {
        var updated = riskZoneService.updateRiskZone(id, request.toEntity());
        return ResponseEntity.ok(RiskZoneResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma zona de risco")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        riskZoneService.deleteRiskZone(id);
    }
}