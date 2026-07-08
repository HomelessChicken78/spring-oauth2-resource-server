package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.*;
import it.itsacademy.springoauth2resourceserver.exception.*;
import it.itsacademy.springoauth2resourceserver.model.*;
import it.itsacademy.springoauth2resourceserver.repository.*;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import it.itsacademy.springoauth2resourceserver.mapper.UserProfileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${COGNITO_DOMAIN}")
    private String cognitoDomain;

    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper mapper;
    private final CurrentUserProvider currentUser;
    private final RestClient restClient;

    @Override
    public void signup(UserProfileRegistrationDTO newUser) {
        if ("me".equals(newUser.getNickname()))
            throw new ConflictException("\"me\" is a reserved word. Try using a different nickname");
        if (profileRepository.existsByNickname(newUser.getNickname()))
            throw new ConflictException("Nickname \"" + newUser.getNickname() + "\" is already in use");

        Map<String, String> userInfo = Optional.ofNullable(
                restClient.get()
                        .uri(cognitoDomain + "/oauth2/userInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + currentUser.getJwt().getTokenValue())
                        .retrieve()
                        .body(new ParameterizedTypeReference<Map<String, String>>() {})
        ).orElseGet(Collections::emptyMap);

        String sub = currentUser.getSub();

        User user = userRepository.findBySub(sub)
                .orElseGet(() -> {
                    User created = new User();
                    created.setSub(sub);
                    created.setEmail(userInfo.get("email"));
                    userRepository.saveAndFlush(created);
                    return created;
                });

        if (profileRepository.existsByUser(user)) throw new ConflictException("User has already a profile.");
        UserProfile newProfile = new UserProfile();
        newProfile.setUser(user);
        newProfile.setName(userInfo.get("name"));
        newProfile.setSurname(userInfo.get("family_name"));
        newProfile.setAvatarUrl(newUser.getAvatarUrl());
        newProfile.setNickname(newUser.getNickname());
        newProfile.setBiografia("");
        profileRepository.save(newProfile);
    }

    @Override
    public UserProfileResponseDTO whoAmI() {
        User found = userRepository.findBySubOrElseThrow(currentUser.getSub());
        UserProfile profileOfFound = profileRepository.findFirstByUserOrElseThrow(found);

        return mapper.toDto(profileOfFound);
    }

    @Override
    public UserProfileResponseDTO search(String nickname) {
        return mapper.toDto(profileRepository.findByNicknameOrElseThrow(nickname));
    }

    @Override
    public void disableUser(String nickname) {
        UserProfile profile = profileRepository.findByNicknameOrElseThrow(nickname);
        User user = profile.getUser();

        if (user.getSub().equals(currentUser.getSub())) throw new ConflictException("Can't disable self.");
        if (!user.isActive()) throw new ConflictException("User " + nickname + " is already disabled. Nothing has changed.");
        if (user.getRoles().contains(User.Role.ADMIN)) throw new ConflictException("Admins can't be disabled");

        user.setActive(false);
        userRepository.save(user);
    }
}
