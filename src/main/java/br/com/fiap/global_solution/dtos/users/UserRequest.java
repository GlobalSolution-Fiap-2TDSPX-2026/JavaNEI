package br.com.fiap.global_solution.dtos.users;

import br.com.fiap.global_solution.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "name is required")
        String name,

        @NotBlank(message = "email is required")
        @Email
        String email,

        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "password is required")
        @Size(min = 8, max = 20, message = "the password must have at least eight characters and in the max, twenty")
        String password

) {

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .username(username)
                .password(password)
                .build();
    }

}
