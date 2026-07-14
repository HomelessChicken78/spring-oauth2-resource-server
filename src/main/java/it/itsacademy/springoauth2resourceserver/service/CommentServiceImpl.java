package it.itsacademy.springoauth2resourceserver.service;

import it.itsacademy.springoauth2resourceserver.dto.common.PageResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.comment.*;
import it.itsacademy.springoauth2resourceserver.exception.ConflictException;
import it.itsacademy.springoauth2resourceserver.mapper.CommentMapper;
import it.itsacademy.springoauth2resourceserver.model.Comment;
import it.itsacademy.springoauth2resourceserver.repository.CommentRepository;
import it.itsacademy.springoauth2resourceserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper mapper;

    @Value("${PAGE_SIZE:3}")
    private int pageSize;

    @Override
    public CommentResponseDTO createComment(ObjectId postId, CommentCreationRequestDTO request) {
        postRepository.existsByIdOrElseThrow(postId);

        Comment comment = mapper.toEntity(request);
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
    public void deleteComment(ObjectId postId, String commentId) {
        postRepository.existsByIdOrElseThrow(postId);
    }

    @Override
    public CommentResponseDTO editComment(ObjectId postId, EditCommentRequestDTO request) {
        postRepository.existsByIdOrElseThrow(postId);
        return null;
    }
}
