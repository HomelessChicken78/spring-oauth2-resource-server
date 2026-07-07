package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.exception.NotFoundException;
import it.itsacademy.springoauth2resourceserver.model.User;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    boolean existsByUser(User user);

    Optional<UserProfile> findFirstByUser(User user);

    default UserProfile findFirstByUserOrElseThrow(User user) {
        return findFirstByUser(user)
                .orElseThrow(
                        () -> new NotFoundException("The user " + user.getUserId() + " doesn't have any profile")
                );
    }

    Optional<UserProfile> findByNickname(String nickname);

    default UserProfile findByNicknameOrElseThrow(String nickname) {
        return findByNickname(nickname)
                .orElseThrow(
                        () -> new NotFoundException("Could not find any user with nickname " + nickname)
                );
    }

    boolean existsByNickname(String nickname);
}
