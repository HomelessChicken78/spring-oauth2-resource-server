package it.itsacademy.springoauth2resourceserver.mapper;

import it.itsacademy.springoauth2resourceserver.dto.post.comment.CommentCreationRequestDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.comment.CommentResponseDTO;
import it.itsacademy.springoauth2resourceserver.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class})
public interface CommentMapper {
    @Mapping(target = "postId", ignore = true)
    Comment toEntity(CommentResponseDTO dto);

    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CommentCreationRequestDTO dto);

    CommentResponseDTO toDto(Comment entity);
}
