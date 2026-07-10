package it.itsacademy.springoauth2resourceserver.security;

import it.itsacademy.springoauth2resourceserver.exception.UnauthorizedException;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import it.itsacademy.springoauth2resourceserver.repository.UserProfileRepository;
import it.itsacademy.springoauth2resourceserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class CurrentUserProvider {
    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;

    public Jwt getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return (Jwt) auth.getPrincipal();
    }

    public String getSub() {
        return getJwt().getSubject();
    }

    public UserProfile getProfile() {
        return profileRepository.findFirstByUserOrElseThrow(
                userRepository.findBySubOrElseThrow(getSub())
        );
    }
}