package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.dto.UserProfileResponseDTO;
import it.itsacademy.springoauth2resourceserver.service.AuthService;
import jakarta.validation.Valid;
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
    public void signup(@RequestBody @Valid UserProfileRegistrationDTO newUser) {
        authService.signup(newUser);
    }

    @GetMapping(path = "/me", produces = json)
    public UserProfileResponseDTO whoAmI() {
        return authService.whoAmI();
    }
    //@PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMIN')")
}
