package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.post.comment.*;
import it.itsacademy.springoauth2resourceserver.service.CommentService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.*;
import it.itsacademy.springoauth2resourceserver.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    private ObjectId parseObjectId(String idPost) {
        if (!ObjectId.isValid(idPost)) throw new BadRequestException("Invalid post id");

        return new ObjectId(idPost);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public CommentResponseDTO createComment(@PathVariable String postId, @RequestBody @Valid CommentCreationRequestDTO request) {
        return service.createComment(parseObjectId(postId), request);
    }

    @GetMapping(params = "page", produces = APPLICATION_JSON_VALUE)
    public PageResponseDTO<List<CommentResponseDTO>> getAllCommentsOfPost(@PathVariable String postId, @RequestParam(defaultValue = "1") int page) {
        return service.getAllCommentsOfPost(parseObjectId(postId), page);
    }
}
