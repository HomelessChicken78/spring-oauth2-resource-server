package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.PostCreationRequestDTO;
import it.itsacademy.springoauth2resourceserver.dto.PostResponseDTO;
import it.itsacademy.springoauth2resourceserver.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public PostResponseDTO createPost(@RequestBody @Valid PostCreationRequestDTO post) {
        return service.createPost(post);
    }

    @DeleteMapping("/{idPost}")
    @ResponseStatus(NO_CONTENT)
    public void deletePost(@PathVariable String idPost) {
        service.deletePost(new ObjectId(idPost));
    }
}
