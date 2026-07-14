package it.itsacademy.springoauth2resourceserver.mapper;

import it.itsacademy.springoauth2resourceserver.dto.post.PostCreationRequestDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.PostResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.ShortPostResponseDTO;
import it.itsacademy.springoauth2resourceserver.model.Post;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class})
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Post toEntity(PostCreationRequestDTO dto);

    @Mapping(target = "relatedPosts", ignore = true)
    PostResponseDTO toDto(Post entity);

    ShortPostResponseDTO toDtoShort(Post entity);
}
