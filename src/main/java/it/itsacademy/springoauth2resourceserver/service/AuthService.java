package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.*;

import java.util.Collection;

public interface AuthService {
    void signup(UserProfileRegistrationDTO newUser);

    UserProfileResponseDTO whoAmI();

    UserProfileResponseDTO search(String nickname);

    void disableUser(String nickname);

    void enableUser(String nickname);

    void changeUserRoles(String nickname, Collection<String> roles);

    UserProfileResponseDTO updateUserBio(String newBiography);

    PageResponseDTO<UserProfileShortResponseDTO> followersOf(String nickname, int page);

    PageResponseDTO<UserProfileShortResponseDTO> followingOf(String nickname, int page);

    void follow(String followingNickname);

    void unfollow(String followingNickname);
}
