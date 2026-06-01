package br.com.fiap.global_solution.controllers;

import br.com.fiap.global_solution.services.NasaSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/nasa")
@Tag(name = "NASA Sync", description = "Sincronização de asteroides com a API da NASA (NeoWs)")
public class NasaSyncController {

    private final NasaSyncService nasaSyncService;

    public NasaSyncController(NasaSyncService nasaSyncService) {
        this.nasaSyncService = nasaSyncService;
    }

    @PostMapping("/sync/today")
    @Operation(
            summary = "Sincronizar asteroides de hoje com a NASA",
            description = "Busca os Near Earth Objects de hoje na API da NASA, salva no banco e gera alertas automáticos para todas as zonas de risco cadastradas."
    )
    public ResponseEntity<Map<String, Object>> syncToday() {
        int count = nasaSyncService.syncAsteroidsFromToday();
        return ResponseEntity.ok(Map.of(
                "message", "Sincronização concluída",
                "date", LocalDate.now().toString(),
                "approximationsProcessed", count
        ));
    }

    @PostMapping("/sync")
    @Operation(
            summary = "Sincronizar asteroides por intervalo de datas",
            description = "Busca Near Earth Objects entre as datas informadas (máximo 7 dias, limitação da API da NASA). Gera alertas para todas as zonas de risco cadastradas."
    )
    public ResponseEntity<Map<String, Object>> syncByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "endDate não pode ser anterior a startDate"
            ));
        }
        if (startDate.plusDays(7).isBefore(endDate)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "O intervalo máximo permitido pela API da NASA é de 7 dias"
            ));
        }

        int count = nasaSyncService.syncAsteroids(startDate, endDate);
        return ResponseEntity.ok(Map.of(
                "message", "Sincronização concluída",
                "startDate", startDate.toString(),
                "endDate", endDate.toString(),
                "approximationsProcessed", count
        ));
    }
}