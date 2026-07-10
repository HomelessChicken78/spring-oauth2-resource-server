package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.PostCreationRequestDTO;
import it.itsacademy.springoauth2resourceserver.dto.PostResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.ShortPostResponseDTO;
import org.bson.types.ObjectId;

import java.util.Set;

public interface PostService {
    PostResponseDTO createPost(PostCreationRequestDTO post);

    void deletePost(ObjectId idPost);
    
    Set<ShortPostResponseDTO> searchPosts(String title, String author);

    PostResponseDTO findPost(ObjectId idPost);

    PostResponseDTO changeTitle(ObjectId idPost, String changedTitle);

    PostResponseDTO changeContent(ObjectId idPost, String changedContent);

    PostResponseDTO addRelatedPost(ObjectId postId, ObjectId relatedPostId);

    PostResponseDTO removeRelatedPost(ObjectId idPost, ObjectId relatedPostId);
}
