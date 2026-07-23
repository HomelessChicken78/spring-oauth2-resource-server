package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileResponseDTO;
import it.itsacademy.springoauth2resourceserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/me", produces = json)
    public UserProfileResponseDTO whoAmI() {
        return authService.whoAmI();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/{nickname}", produces = json)
    public UserProfileResponseDTO searchUserByNickname(@PathVariable String nickname) {
        return authService.search(nickname);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMIN')")
    @PatchMapping(path = "/users/{nickname}/disable", produces = json)
    public void disableUser(@PathVariable String nickname) {
        authService.disableUser(nickname);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMIN')")
    @PatchMapping(path = "/users/{nickname}/enable", produces = json)
    public void enableUser(@PathVariable String nickname) {
        authService.enableUser(nickname);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping(path = "/users/{nickname}/changeUserRoles", consumes = json)
    public void changeUserRoles(@PathVariable String nickname, @RequestBody Collection<String> roles) {
        authService.changeUserRoles(nickname, roles);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "/users/me/updateUserBio", produces = json, consumes = json)
    public UserProfileResponseDTO updateUserBio(@RequestBody String newBiography) {
        return authService.updateUserBio(newBiography);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/users/me/follow/{followingNickname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void followUser(@PathVariable String followingNickname) {
        authService.follow(followingNickname);
    }
}
