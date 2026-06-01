package br.com.fiap.global_solution.dtos.users;

import br.com.fiap.global_solution.enums.Role;
import br.com.fiap.global_solution.models.User;

public record UserResponse(

        Long id,
        String username,
        Role role
) {

    public static UserResponse fromEntity(User u){
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }
}
