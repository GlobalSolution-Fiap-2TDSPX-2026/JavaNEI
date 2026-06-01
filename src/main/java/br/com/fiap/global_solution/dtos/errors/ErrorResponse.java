package br.com.fiap.global_solution.dtos.errors;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse (
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
){
}
