package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.model.User;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    public boolean existsByUser(User user);
}
