package br.com.fiap.global_solution.dtos.users;

import br.com.fiap.global_solution.enums.Role;
import br.com.fiap.global_solution.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "password is required")
        @Size(min = 8, max = 20, message = "the password must have at least eight characters and in the max, twenty")
        String password,

        @NotNull
        Role role
) {

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

}
