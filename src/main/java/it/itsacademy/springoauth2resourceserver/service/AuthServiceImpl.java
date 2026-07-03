package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.UserProfileRegistrationDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class AuthServiceImpl implements AuthService {
    @Override
    public void signup(UserProfileRegistrationDTO newUser) {

    }
}
