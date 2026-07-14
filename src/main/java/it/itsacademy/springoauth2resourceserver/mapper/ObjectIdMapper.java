package it.itsacademy.springoauth2resourceserver.mapper;

import it.itsacademy.springoauth2resourceserver.exception.BadRequestException;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObjectIdMapper {
    default ObjectId toObjectId(String string) {
        if (!ObjectId.isValid(string)) throw new BadRequestException("Invalid id");

        return new ObjectId(string);
    }

    default String fromObjectIdToString(ObjectId objectId) {
        if (objectId == null) return null;
        return objectId.toHexString();
    }
}
