package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.*;
import it.itsacademy.springoauth2resourceserver.exception.BadRequestException;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import it.itsacademy.springoauth2resourceserver.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;
    private final CurrentUserProvider currentUser;

    private ObjectId parseObjectId(String idPost) {
        if (!ObjectId.isValid(idPost)) throw new BadRequestException("Invalid post id");

        return new ObjectId(idPost);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public PostResponseDTO createPost(@RequestBody @Valid PostCreationRequestDTO post) {
        return service.createPost(post);
    }

    @DeleteMapping("/{idPost}")
    @ResponseStatus(NO_CONTENT)
    public void deletePost(@PathVariable String idPost) {
        service.deletePost(parseObjectId(idPost));
    }

    @GetMapping("/search")
    public PageResponseDTO<List<ShortPostResponseDTO>> searchPosts(String title, String author, String topic, @RequestParam(defaultValue = "1") int page) {
        if ("me".equals(author)) return service.searchPosts(title, currentUser.getProfile().getNickname(), topic, page);
        return service.searchPosts(title, author, topic, page);
    }

    @GetMapping("/{idPost}")
    public PostResponseDTO findPost(@PathVariable String idPost) {
        return service.findPost(parseObjectId(idPost));
    }

    @PatchMapping("/{idPost}/changeTitle")
    public PostResponseDTO changeTitle(@PathVariable String idPost, @RequestBody @Valid TitleChangeRequestDTO request) {
        return service.changeTitle(parseObjectId(idPost), request);
    }

    @PutMapping("/{idPost}/changeContent")
    public PostResponseDTO changeContent(@PathVariable String idPost, @RequestBody @Valid ContentChangeRequestDTO request) {
        return service.changeContent(parseObjectId(idPost), request);
    }

    @PutMapping("/{idPost}/relatedPosts/{idRelatedPost}")
    public PostResponseDTO addRelatedPost(@PathVariable String idPost, @PathVariable String idRelatedPost) {
        return service.addRelatedPost(parseObjectId(idPost), parseObjectId(idRelatedPost));
    }

    @DeleteMapping("/{idPost}/relatedPosts/{idRelatedPost}")
    public PostResponseDTO removeRelatedPost(@PathVariable String idPost, @PathVariable String idRelatedPost) {
        return service.removeRelatedPost(parseObjectId(idPost), parseObjectId(idRelatedPost));
    }
}
