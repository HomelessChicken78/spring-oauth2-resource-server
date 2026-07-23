package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.user.*;

import java.util.Collection;
import java.util.List;

public interface AuthService {
    void signup(UserProfileRegistrationDTO newUser);

    UserProfileResponseDTO whoAmI();

    UserProfileResponseDTO search(String nickname);

    void disableUser(String nickname);

    void enableUser(String nickname);

    void changeUserRoles(String nickname, Collection<String> roles);

    UserProfileResponseDTO updateUserBio(String newBiography);

    List<UserProfileShortResponseDTO> followersOf(String nickname);

    List<UserProfileShortResponseDTO> followingOf(String nickname);

    void follow(String followingNickname);

    void unfollow(String followingNickname);
}
