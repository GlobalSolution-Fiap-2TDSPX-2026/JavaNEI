package br.com.fiap.global_solution.dtos.users;

public record LoginResponse(

        Long id,
        String name,
        String email,
        String username,
        String token
) {
}
