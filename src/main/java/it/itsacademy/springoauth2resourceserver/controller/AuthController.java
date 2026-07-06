package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.dto.UserProfileResponseDTO;
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
    public void signup(@RequestBody UserProfileRegistrationDTO newUser, @RequestHeader("id-token") String idToken) {
        // NB: non è la soluzione "corretta" passare l'id token al resource server. Se proprio serve il contenuto si può
        // usare OICD (eg con GET su /oauth2/userInfo)
        authService.signup(newUser, idToken);
    }

    @GetMapping(path = "/me", produces = json)
    public UserProfileResponseDTO whoAmI() {
        return authService.whoAmI();
    }
}
