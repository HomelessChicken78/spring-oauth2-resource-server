package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/posts")
public class PostController {
    private static final String json = "application/json";
    private PostService service;
}
