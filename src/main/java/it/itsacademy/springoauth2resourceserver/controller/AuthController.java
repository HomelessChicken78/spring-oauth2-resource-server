package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final String json = "application/json";
    private final AuthService authService;

    @PutMapping(path = "/signup", consumes = json)
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(UserProfileRegistrationDTO newUser) {
        authService.signup(newUser);
    }
}
