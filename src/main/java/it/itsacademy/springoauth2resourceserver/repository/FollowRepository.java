package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.model.Follow;
import it.itsacademy.springoauth2resourceserver.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    @Query("SELECT f.follower FROM Follow f WHERE f.following.idProfile = :followingId")
    Page<UserProfile> findFollowersByFollowing(@Param("followingId") UUID followingId, Pageable page);

    @Query("SELECT f.following FROM Follow f WHERE f.follower.idProfile = :followerId")
    Page<UserProfile> findFollowingByFollower(@Param("followerId") UUID followerId, Pageable page);
}
