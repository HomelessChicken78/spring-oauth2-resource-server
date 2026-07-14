package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileResponseDTO;

import java.util.Collection;

public interface AuthService {
    void signup(UserProfileRegistrationDTO newUser);

    UserProfileResponseDTO whoAmI();

    UserProfileResponseDTO search(String nickname);

    void disableUser(String nickname);

    void enableUser(String nickname);

    void changeUserRoles(String nickname, Collection<String> roles);

    UserProfileResponseDTO updateUserBio(String newBiography);
}
