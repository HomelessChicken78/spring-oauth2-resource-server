package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findBySub(String sub);
}
