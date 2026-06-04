package br.com.fiap.global_solution.dtos.users;

public record LoginResponse(

        String email,
        String token
) {
}
