package br.com.fiap.global_solution.services;


import br.com.fiap.global_solution.models.User;
import br.com.fiap.global_solution.repositories.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(Long id, User newUser) {
        var optionalUser = findById(id);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        newUser.setId(id);
        return userRepository.save(newUser);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        var optionalUser = findById(id);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        userRepository.deleteById(id);
    }

}
