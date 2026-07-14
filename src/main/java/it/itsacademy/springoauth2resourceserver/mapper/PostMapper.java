package it.itsacademy.springoauth2resourceserver.mapper;

import it.itsacademy.springoauth2resourceserver.dto.post.PostCreationRequestDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.PostResponseDTO;
import it.itsacademy.springoauth2resourceserver.dto.post.ShortPostResponseDTO;
import it.itsacademy.springoauth2resourceserver.exception.BadRequestException;
import it.itsacademy.springoauth2resourceserver.model.Post;
import org.bson.types.ObjectId;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Post toEntity(PostCreationRequestDTO dto);

    @Mapping(target = "relatedPosts", ignore = true)
    PostResponseDTO toDto(Post entity);

    ShortPostResponseDTO toDtoShort(Post entity);

    default ObjectId toObjectId(String string) {
        if (!ObjectId.isValid(string)) throw new BadRequestException("Invalid id");

        return new ObjectId(string);
    }

    default String fromObjectIdToString(ObjectId objectId) {
        if (objectId == null) return null;
        return objectId.toHexString();
    }
}
