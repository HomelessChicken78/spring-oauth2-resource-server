package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.*;
import it.itsacademy.springoauth2resourceserver.exception.*;
import it.itsacademy.springoauth2resourceserver.model.*;
import it.itsacademy.springoauth2resourceserver.repository.*;
import lombok.RequiredArgsConstructor;
import it.itsacademy.springoauth2resourceserver.mapper.UserProfileMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.HashMap;

@Service @Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper mapper;

    private Jwt getAccessToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return (Jwt) auth.getPrincipal();
    }

    @Override
    public void signup(UserProfileRegistrationDTO newUser, String idToken) {
        String[] chunks = idToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> map = mapper.readValue(
                payload,
                new TypeReference<>() {}
        );

        String sub = getAccessToken().getSubject();

        User user = userRepository.findBySub(sub)
                .orElseGet(() -> {
                    User created = new User();
                    created.setSub(sub);
                    created.setEmail(map.get("email"));
                    userRepository.saveAndFlush(created);
                    return created;
                });

        if (profileRepository.existsByUser(user)) throw new ConflictException("User has already a profile.");
        UserProfile newProfile = new UserProfile();
        newProfile.setUser(user);
        newProfile.setName(map.get("name"));
        newProfile.setSurname(map.get("family_name"));
        newProfile.setAvatarUrl(newUser.getAvatarUrl());
        newProfile.setBiografia("");
        profileRepository.save(newProfile);
    }

    @Override
    public UserProfileResponseDTO whoAmI() {
        User found = userRepository.findBySubOrElseThrow(getAccessToken().getSubject());
        UserProfile profileOfFound = profileRepository.findFirstByUserOrElseThrow(found);

        return mapper.toDto(profileOfFound);
    }
}
