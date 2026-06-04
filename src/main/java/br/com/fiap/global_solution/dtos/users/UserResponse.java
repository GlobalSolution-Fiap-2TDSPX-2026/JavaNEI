package br.com.fiap.global_solution.dtos.users;

import br.com.fiap.global_solution.models.User;

public record UserResponse(

        Long id,
        String name,
        String email,
        String username

        ) {

    public static UserResponse fromEntity(User u){
        return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getUsername());
    }
}
