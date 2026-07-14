package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.PostCreationRequestDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.PostResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.ShortPostResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.TitleChangeRequestDTO;
import it.itsacademy.springoauth2resourceserver.exception.ConflictException;
import it.itsacademy.springoauth2resourceserver.mapper.PostMapper;
import it.itsacademy.springoauth2resourceserver.model.*;
import it.itsacademy.springoauth2resourceserver.repository.PostRepository;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service @Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper mapper;
    private final CurrentUserProvider currentUser;
    private final MongoTemplate mongoTemplate;

    @Value("${PAGE_SIZE:3}")
    private int pageSize;

    // Search all related posts to check if they exist and map them
    private Set<ShortPostResponseDTO> mapRelatedPosts(Set<ObjectId> relatedPosts) {
        if (relatedPosts == null || relatedPosts.isEmpty()) return new HashSet<>();

        return relatedPosts.stream()
                .map(postRepository::findByIdOrElseThrow)
                .map(mapper::toDtoShort)
                .collect(Collectors.toSet());
    }

    private boolean validateActionPrivileges(UserProfile requestingUser, Post resource) {
        if (requestingUser == null || resource == null) return false;

        Set<User.Role> roles = requestingUser.getUser().getRoles();
        boolean isAuthor = requestingUser.getNickname().equals(resource.getAuthor());
        boolean hasPrivilegedRole = roles.contains(User.Role.ADMIN) || roles.contains(User.Role.MANAGER);

        return isAuthor || hasPrivilegedRole;
    }

    @Override
    public PostResponseDTO createPost(PostCreationRequestDTO post) {
        Post postToSave = mapper.toEntity(post);
        postToSave.setAuthor(currentUser.getProfile().getNickname());

        PostResponseDTO response = mapper.toDto(postToSave);
        response.setRelatedPosts(mapRelatedPosts(postToSave.getRelatedPosts()));

        response.setId(postRepository.save(postToSave).getId().toHexString());
        return response;
    }

    @Override
    public void deletePost(ObjectId idPost) {
        UserProfile requestingUser = currentUser.getProfile();
        Post toDelete = postRepository.findByIdOrElseThrow(idPost);

        if (!validateActionPrivileges(requestingUser, toDelete))
            throw new ConflictException("Only the author, an admin, or a manager can remove the post.");

        postRepository.delete(toDelete);
    }

    @Override
    public PageResponseDTO<List<ShortPostResponseDTO>> searchPosts(String title, String author, String topic, int page) {
        if (page < 1) throw new ConflictException("Page number must be greater or equal than one.");

        Query query = new Query();

        if (title != null && !title.isBlank())
            query.addCriteria(Criteria.where("title").is(title));

        if (author != null && !author.isBlank()) {
            if ("me".equals(author))
                query.addCriteria(Criteria.where("author").is(currentUser.getProfile().getNickname()));
            else
                query.addCriteria(Criteria.where("author").is(author));
        }

        if (topic != null && !topic.isBlank())
            query.addCriteria(Criteria.where("topic").is(topic));

        long totalElements = mongoTemplate.count(query, Post.class);

        query.with(PageRequest.of(page - 1, pageSize));

        List<ShortPostResponseDTO> content = mongoTemplate.find(query, Post.class)
                .stream().map(mapper::toDtoShort)
                .toList();

        return PageResponseDTO.<List<ShortPostResponseDTO>>builder()
                .content(content)
                .currentPage(page)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / pageSize))
                .build();
    }

    @Override
    public PostResponseDTO findPost(ObjectId idPost) {
        Post found = postRepository.findByIdOrElseThrow(idPost);
        PostResponseDTO response = mapper.toDto(found);

        response.setRelatedPosts(mapRelatedPosts(found.getRelatedPosts()));

        return response;
    }

    @Override
    public PostResponseDTO changeTitle(ObjectId idPost, TitleChangeRequestDTO request) {
        Post post = postRepository.findByIdOrElseThrow(idPost);

        post.setTitle(request.getTitle());
        postRepository.save(post);
        return mapper.toDto(post);
    }

    @Override
    public PostResponseDTO changeContent(ObjectId idPost, String changedContent) {
        return null;
    }

    @Override
    public PostResponseDTO addRelatedPost(ObjectId postId, ObjectId relatedPostId) {
        return null;
    }

    @Override
    public PostResponseDTO removeRelatedPost(ObjectId idPost, ObjectId relatedPostId) {
        return null;
    }
}
