package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.PostCreationRequestDTO;
import it.itsacademy.springoauth2resourceserver.dto.PostResponseDTO;
import it.itsacademy.springoauth2resourceserver.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private static final String json = "application/json";
    private final PostService service;

    @PostMapping(produces = json, consumes = json)
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDTO createPost(@RequestBody @Valid PostCreationRequestDTO post) {
        return service.createPost(post);
    }
}
