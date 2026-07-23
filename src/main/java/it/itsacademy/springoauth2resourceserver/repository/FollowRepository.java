package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.model.Follow;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    @Query("SELECT f.follower FROM Follow f WHERE f.following = :followingId")
    List<UserProfile> findFollowersByFollowing(UUID followingId);

    @Query("SELECT f.following FROM Follow f WHERE f.follower = :followerId")
    List<UserProfile> findFollowingByFollower(UUID followerId);
}
