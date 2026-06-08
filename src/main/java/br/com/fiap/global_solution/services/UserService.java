package br.com.fiap.global_solution.services;


import br.com.fiap.global_solution.models.User;
import br.com.fiap.global_solution.repositories.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "users")
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");

        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(Long id, User newUser) {

        var optionalUser = findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }

        newUser.setId(id);

        // criptografa a senha
        newUser.setPassword(
                passwordEncoder.encode(newUser.getPassword())
        );

        return userRepository.save(newUser);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        var optionalUser = findById(id);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        userRepository.deleteById(id);
    }

}
