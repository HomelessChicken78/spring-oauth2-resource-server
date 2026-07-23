package it.itsacademy.springoauth2resourceserver.controller;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileShortResponseDTO;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import it.itsacademy.springoauth2resourceserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CurrentUserProvider currentUser;

    @PutMapping(path = "/signup", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid UserProfileRegistrationDTO newUser) {
        authService.signup(newUser);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/me", produces = APPLICATION_JSON_VALUE)
    public UserProfileResponseDTO whoAmI() {
        return authService.whoAmI();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/{nickname}", produces = APPLICATION_JSON_VALUE)
    public UserProfileResponseDTO searchUserByNickname(@PathVariable String nickname) {
        return authService.search(nickname);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMIN')")
    @PatchMapping(path = "/users/{nickname}/disable", produces = APPLICATION_JSON_VALUE)
    public void disableUser(@PathVariable String nickname) {
        authService.disableUser(nickname);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_ADMIN')")
    @PatchMapping(path = "/users/{nickname}/enable", produces = APPLICATION_JSON_VALUE)
    public void enableUser(@PathVariable String nickname) {
        authService.enableUser(nickname);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping(path = "/users/{nickname}/changeUserRoles", consumes = APPLICATION_JSON_VALUE)
    public void changeUserRoles(@PathVariable String nickname, @RequestBody Collection<String> roles) {
        authService.changeUserRoles(nickname, roles);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "/users/me/updateUserBio", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public UserProfileResponseDTO updateUserBio(@RequestBody String newBiography) {
        return authService.updateUserBio(newBiography);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/users/me/follow/{followingNickname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void followUser(@PathVariable String followingNickname) {
        authService.follow(followingNickname);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/{nickname}/followers")
    public PageResponseDTO<UserProfileShortResponseDTO> getFollowers(@PathVariable String nickname, @RequestParam(defaultValue = "1") int page) {
        if ("me".equals(nickname)) return authService.followersOf(currentUser.getProfile().getNickname(), page);
        return authService.followersOf(nickname, page);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/users/{nickname}/followings")
    public PageResponseDTO<UserProfileShortResponseDTO> getFollowings(@PathVariable String nickname, @RequestParam(defaultValue = "1") int page) {
        if ("me".equals(nickname)) return authService.followingOf(currentUser.getProfile().getNickname(), page);
        return authService.followingOf(nickname, page);
    }
}
