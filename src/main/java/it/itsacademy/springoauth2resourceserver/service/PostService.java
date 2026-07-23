package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.*;
import org.bson.types.ObjectId;

public interface PostService {
    PostResponseDTO createPost(PostCreationRequestDTO post);

    void deletePost(ObjectId idPost);

    PageResponseDTO<ShortPostResponseDTO> searchPosts(String title, String author, String topic, int page);

    PostResponseDTO findPost(ObjectId idPost);

    PostResponseDTO changeTitle(ObjectId idPost, TitleChangeRequestDTO request);

    PostResponseDTO changeContent(ObjectId idPost, ContentChangeRequestDTO request);

    PostResponseDTO addRelatedPost(ObjectId postId, ObjectId relatedPostId);

    PostResponseDTO removeRelatedPost(ObjectId idPost, ObjectId relatedPostId);
}
