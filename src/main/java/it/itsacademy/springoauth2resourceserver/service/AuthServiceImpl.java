package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileRegistrationDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.user.UserProfileShortResponseDTO;
import it.itsacademy.springoauth2resourceserver.exception.*;
import it.itsacademy.springoauth2resourceserver.mapper.PageMapper;
import it.itsacademy.springoauth2resourceserver.model.*;
import it.itsacademy.springoauth2resourceserver.repository.*;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import it.itsacademy.springoauth2resourceserver.mapper.UserProfileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service @Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${COGNITO_DOMAIN}")
    private String cognitoDomain;

    @Value("#{'${AVATAR_IMAGE_FORMATS:image/jpg}'.split(',')}")
    private List<String> avatarImageFormats;

    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final UserProfileMapper mapper;
    private final PageMapper pageMapper;
    private final CurrentUserProvider currentUser;
    private final RestClient restClient;

    @Value("${USER_PAGE_SIZE:10}")
    private int pageSize;

    @Value("${S3_BUCKET_NAME}")
    private String bucketS3;

    @Value("${S3_ROOT_PREFIX}")
    private String rootPrefix;

    private boolean isValidRole(String role) {
        return role != null &&
                Arrays.stream(User.Role.values())
                        .anyMatch(r -> r.name().equals(role));
    }

    @Override
    public void signup(UserProfileRegistrationDTO newUser) {
        if ("me".equals(newUser.getNickname()))
            throw new ConflictException("\"me\" is a reserved word. Try using a different nickname");
        if (profileRepository.existsByNickname(newUser.getNickname()))
            throw new ConflictException("Nickname \"" + newUser.getNickname() + "\" is already in use");

        Map<String, String> userInfo = Optional.ofNullable(
                restClient.get()
                        .uri(cognitoDomain + "/oauth2/userInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + currentUser.getJwt().getTokenValue())
                        .retrieve()
                        .body(new ParameterizedTypeReference<Map<String, String>>() {})
        ).orElseGet(Collections::emptyMap);

        String sub = currentUser.getSub();

        User user = userRepository.findBySub(sub)
                .orElseGet(() -> {
                    User created = new User();
                    created.setSub(sub);
                    created.setEmail(userInfo.get("email"));
                    userRepository.saveAndFlush(created);
                    return created;
                });

        if (profileRepository.existsByUser(user)) throw new ConflictException("User has already a profile.");
        UserProfile newProfile = new UserProfile();
        newProfile.setUser(user);
        newProfile.setName(userInfo.get("name"));
        newProfile.setSurname(userInfo.get("family_name"));
        newProfile.setAvatarUrl(newUser.getAvatarUrl());
        newProfile.setNickname(newUser.getNickname());
        newProfile.setBiografia("");
        profileRepository.save(newProfile);
    }

    @Override
    public UserProfileResponseDTO whoAmI() {
        return mapper.toDto(currentUser.getProfile());
    }

    @Override
    public UserProfileResponseDTO search(String nickname) {
        return mapper.toDto(profileRepository.findByNicknameOrElseThrow(nickname));
    }

    @Override
    public void disableUser(String nickname) {
        UserProfile profile = profileRepository.findByNicknameOrElseThrow(nickname);
        User user = profile.getUser();

        if (user.getSub().equals(currentUser.getSub())) throw new ConflictException("Can't disable self.");
        if (!user.isActive()) throw new ConflictException("User " + nickname + " is already disabled. Nothing has changed.");
        if (user.getRoles().contains(User.Role.ADMIN)) throw new ConflictException("Admins can't be disabled");

        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(String nickname) {
        UserProfile profile = profileRepository.findByNicknameOrElseThrow(nickname);
        User user = profile.getUser();

        if (user.getSub().equals(currentUser.getSub())) throw new ConflictException("Can't enable self.");
        if (user.isActive()) throw new ConflictException("User " + nickname + " is already enabled. Nothing has changed.");

        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void changeUserRoles(String nickname, Collection<String> roles) {
        UserProfile profile = profileRepository.findByNicknameOrElseThrow(nickname);
        User user = profile.getUser();

        if (user.getSub().equals(currentUser.getSub())) throw new ConflictException("Can't change own roles.");
        if (user.getRoles().contains(User.Role.ADMIN)) throw new ConflictException("Can't change roles of an admin.");
        if (roles.isEmpty()) throw new ConflictException("Each user must have at least one role.");

        user.setRoles(new HashSet<>());
        for (String role : roles) {
            if (!isValidRole(role)) throw new ConflictException("Invalid role: " + role);
            if ("ADMIN".equals(role)) throw new ConflictException("Can't add role \"ADMIN\" to an user.");
            user.getRoles().add(User.Role.valueOf(role));
        }
        userRepository.save(user);
    }

    @Override
    public UserProfileResponseDTO updateUserBio(String newBiography) {
        User found = userRepository.findBySubOrElseThrow(currentUser.getSub());
        UserProfile profileOfFound = profileRepository.findFirstByUserOrElseThrow(found);

        profileOfFound.setBiografia(newBiography);

        profileRepository.save(profileOfFound);
        return mapper.toDto(profileOfFound);
    }

    @Override
    public PageResponseDTO<UserProfileShortResponseDTO> followersOf(String nickname, int page) {
        if (page < 1) throw new BadRequestException("Page number must be greater or equal than one.");

        UserProfile following = profileRepository.findByNicknameOrElseThrow(nickname);
        Page<UserProfile> followers = followRepository.findFollowersByFollowing(following.getIdProfile(), PageRequest.of(page - 1, pageSize));
        System.out.println(followers.getContent().size());

        return pageMapper.toDto(followers, mapper::toShortDto);
    }

    @Override
    public PageResponseDTO<UserProfileShortResponseDTO> followingOf(String nickname, int page) {
        if (page < 1) throw new BadRequestException("Page number must be greater or equal than one.");

        UserProfile follower = profileRepository.findByNicknameOrElseThrow(nickname);
        Page<UserProfile> followings = followRepository.findFollowingByFollower(follower.getIdProfile(), PageRequest.of(page - 1, pageSize));

        return pageMapper.toDto(followings, mapper::toShortDto);
    }

    @Override
    public void follow(String followingNickname) {
        UserProfile following = profileRepository.findByNicknameOrElseThrow(followingNickname);
        UserProfile follower = currentUser.getProfile();

        if (following.getIdProfile().equals(follower.getIdProfile())) throw new ConflictException("Can't follow oneself.");

        Follow follows = Follow.builder()
                .following(following)
                .follower(follower)
                .build();

        try {
            followRepository.saveAndFlush(follows);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ConflictException("Already following user " + followingNickname + ".", e);
        }

        following.setFollowers(following.getFollowers() + 1);
        profileRepository.save(following);

        follower.setFollowings(follower.getFollowings() + 1);
        profileRepository.save(follower);
    }

    @Override
    public void unfollow(String followingNickname) {
        UserProfile following = profileRepository.findByNicknameOrElseThrow(followingNickname);
        UserProfile follower = currentUser.getProfile();

        Follow follows = followRepository.findByFollowerAndFollowingOrElseThrow(follower, following);
        followRepository.delete(follows);

        following.setFollowers(following.getFollowers() - 1);
        profileRepository.save(following);

        follower.setFollowings(follower.getFollowings() - 1);
        profileRepository.save(follower);
    }

    @Override
    public void uploadAvatarUrl(MultipartFile image) {
    }
}
