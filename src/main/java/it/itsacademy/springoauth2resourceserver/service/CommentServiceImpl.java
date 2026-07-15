package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.comment.*;
import it.itsacademy.springoauth2resourceserver.exception.*;
import it.itsacademy.springoauth2resourceserver.mapper.CommentMapper;
import it.itsacademy.springoauth2resourceserver.model.*;
import it.itsacademy.springoauth2resourceserver.repository.*;
import it.itsacademy.springoauth2resourceserver.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service @Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CurrentUserProvider currentUser;
    private final CommentMapper mapper;

    @Value("${PAGE_SIZE:3}")
    private int pageSize;

    private void validateActionPrivileges(UserProfile requestingUser, Comment resource, Post commentPost, String actionName) {
        if (requestingUser == null || resource == null) throw new ConflictException("Requesting user and resource must not be null.");

        Set<User.Role> roles = requestingUser.getUser().getRoles();
        boolean isAuthor = requestingUser.getNickname().equals(resource.getAuthor());
        boolean isPostAuthor = requestingUser.getNickname().equals(commentPost.getAuthor());
        boolean hasPrivilegedRole = roles.contains(User.Role.ADMIN) || roles.contains(User.Role.MANAGER);

        if (!isAuthor && !isPostAuthor && !hasPrivilegedRole)
            throw new ConflictException("Only the post author, the comment author, an admin, or a manager can " + actionName + " a comment of the post.");
    }

    @Override
    public CommentResponseDTO createComment(ObjectId postId, CommentCreationRequestDTO request) {
        postRepository.existsByIdOrElseThrow(postId);

        Comment comment = mapper.toEntity(request);
        comment.setAuthor(currentUser.getProfile().getNickname());
        comment.setPostId(postId);

        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public PageResponseDTO<List<CommentResponseDTO>> getAllCommentsOfPost(ObjectId postId, int page) {
        postRepository.existsByIdOrElseThrow(postId);
        if (page < 1) throw new ConflictException("Page number must be greater or equal than one.");

        Page<Comment> result = commentRepository.findByPostId(postId, PageRequest.of(page - 1, pageSize));
        List<CommentResponseDTO> comments = result.stream()
                .map(mapper::toDto)
                .toList();

        Long totalElements = result.getTotalElements();

        return PageResponseDTO.<List<CommentResponseDTO>>builder()
                .content(comments)
                .currentPage(page)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / pageSize))
                .build();
    }

    @Override
    public void deleteComment(ObjectId postId, ObjectId commentId) {
        Post commentPost = postRepository.findByIdOrElseThrow(postId);
        Comment comment = commentRepository.findByPostIdOrElseThrow(commentId, postId);

        if (!postId.equals(comment.getPostId())) throw new NotFoundException("Could not find any comment with id " + commentId + " of post " + postId);
        validateActionPrivileges(currentUser.getProfile(), comment, commentPost, "delete");

        commentRepository.delete(comment);
    }

    @Override
    public CommentResponseDTO editComment(ObjectId postId, ObjectId commentId, EditCommentRequestDTO request) {
        postRepository.existsByIdOrElseThrow(postId);
        Comment comment = commentRepository.findByPostIdOrElseThrow(commentId, postId);

        if (!postId.equals(comment.getPostId())) throw new NotFoundException("Could not find any comment with id " + commentId + " of post " + postId);

        if (!currentUser.getProfile().getNickname().equals(comment.getAuthor()))
            throw new ConflictException("Only the the comment author can edit a comment of the post.");

        comment.setContent(request.getContent());
        Comment saved = commentRepository.save(comment);

        return mapper.toDto(saved);
    }
}
