package it.itsacademy.springoauth2resourceserver.security;

import it.itsacademy.springoauth2resourceserver.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    public Jwt getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return (Jwt) auth.getPrincipal();
    }

    public String getSub() {
        return getJwt().getSubject();
    }
}