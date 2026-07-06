package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.exception.UnauthorizedException;
import it.itsacademy.springoauth2resourceserver.model.User;
import it.itsacademy.springoauth2resourceserver.repository.UserProfileRepository;
import it.itsacademy.springoauth2resourceserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service @Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Override
    public void signup(UserProfileRegistrationDTO newUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        Jwt jwt = (Jwt) auth.getPrincipal();

        String sub = jwt.getSubject();
        User user = userRepository.findBySub(sub)
                .orElseGet(() -> {
                    User created = new User();
                    created.setSub(sub);
                    created.setEmail(jwt.getClaimAsString("email"));
                    userRepository.save(created);
                    return created;
                });


    }
}
