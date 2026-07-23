package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.comment.*;
import org.bson.types.ObjectId;

public interface CommentService {
    CommentResponseDTO createComment(ObjectId postId, CommentCreationRequestDTO request);

    PageResponseDTO<CommentResponseDTO> getAllCommentsOfPost(ObjectId postId, int page);

    void deleteComment(ObjectId postId, ObjectId commentId);

    CommentResponseDTO editComment(ObjectId postId, ObjectId commentId, EditCommentRequestDTO request);
}
