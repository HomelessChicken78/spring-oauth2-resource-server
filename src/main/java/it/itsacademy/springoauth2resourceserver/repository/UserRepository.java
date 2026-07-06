package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.exception.NotFoundException;
import it.itsacademy.springoauth2resourceserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findBySub(String sub);

    default User findBySubOrElseThrow(String sub) {
        return findBySub(sub)
                .orElseThrow(
                        () -> new NotFoundException("Could not find any user with sub " + sub)
                );
    }
}
