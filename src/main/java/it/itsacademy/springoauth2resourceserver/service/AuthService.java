package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.*;

public interface AuthService {
    void signup(UserProfileRegistrationDTO newUser);

    UserProfileResponseDTO whoAmI();
}
