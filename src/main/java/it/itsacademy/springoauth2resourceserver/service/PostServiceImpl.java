package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.*;
import it.itsacademy.springoauth2resourceserver.mapper.PostMapper;
import it.itsacademy.springoauth2resourceserver.model.Post;
import it.itsacademy.springoauth2resourceserver.repository.PostRepository;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service @Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper mapper;
    private final CurrentUserProvider currentUser;

    @Override
    public PostResponseDTO createPost(PostCreationRequestDTO post) {
        Post postToSave = mapper.toEntity(post);
        postToSave.setAuthor(currentUser.getProfile().getNickname());

        PostResponseDTO response = mapper.toDto(postToSave);
        response.setRelatedPosts(new HashSet<>());

        // Search all related posts to check if they exist and map them
        if (post.getRelatedPosts() != null)
            for (ObjectId p : postToSave.getRelatedPosts())
                response.getRelatedPosts().add(
                        mapper.toDtoShort(postRepository.findByIdOrElseThrow(p))
                );

        response.setId(postRepository.save(postToSave).getId().toHexString());
        return response;
    }

    @Override
    public void deletePost(ObjectId idPost) {

    }

    @Override
    public Set<ShortPostResponseDTO> searchPosts(String title, String author) {
        return Set.of();
    }

    @Override
    public PostResponseDTO findPost(ObjectId idPost) {
        return null;
    }

    @Override
    public PostResponseDTO changeTitle(ObjectId idPost, String changedTitle) {
        return null;
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
