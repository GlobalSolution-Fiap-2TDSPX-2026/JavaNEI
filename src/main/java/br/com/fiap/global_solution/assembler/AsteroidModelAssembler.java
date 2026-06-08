package br.com.fiap.global_solution.assembler;

import br.com.fiap.global_solution.controllers.AsteroidController;
import br.com.fiap.global_solution.controllers.CloseApproachController;
import br.com.fiap.global_solution.dtos.AsteroidResponse;
import br.com.fiap.global_solution.models.Asteroid;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AsteroidModelAssembler extends RepresentationModelAssemblerSupport<Asteroid, AsteroidResponse> {

    public AsteroidModelAssembler() {
        super(AsteroidController.class, AsteroidResponse.class);
    }

    @Override
    public AsteroidResponse toModel(Asteroid asteroid) {
        AsteroidResponse response = AsteroidResponse.fromEntity(asteroid);
        response.add(linkTo(methodOn(AsteroidController.class).getById(asteroid.getId())).withSelfRel());
        response.add(linkTo(methodOn(AsteroidController.class).getAll(null)).withRel("all"));
        response.add(linkTo(AsteroidController.class).slash(asteroid.getId()).withRel("delete"));
        response.add(linkTo(methodOn(CloseApproachController.class).getByAsteroid(asteroid.getId(), null)).withRel("close-approaches"));
        return response;
    }
}