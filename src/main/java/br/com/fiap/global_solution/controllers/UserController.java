package br.com.fiap.global_solution.controllers;

import br.com.fiap.global_solution.dtos.users.UserRequest;
import br.com.fiap.global_solution.dtos.users.UserResponse;
import br.com.fiap.global_solution.services.UserService;
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
@RequestMapping("/users")
@Tag(name = "Users", description = "Gerenciamento de usuários do sistema")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários paginados")
    public ResponseEntity<Page<UserResponse>> getAll(
            @PageableDefault(size = 10, sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable).map(UserResponse::fromEntity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(UserResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo usuário")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        var saved = userService.addUser(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromEntity(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um usuário")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UserRequest request) {
        var updated = userService.updateUser(id, request.toEntity());
        return ResponseEntity.ok(UserResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um usuário")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}