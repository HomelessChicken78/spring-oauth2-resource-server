package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.exception.NotFoundException;
import it.itsacademy.springoauth2resourceserver.model.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
    default Post findByIdOrElseThrow(ObjectId id) {
        return findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Could not find any post with id " + id)
                );
    }

    ObjectId id(ObjectId id);

    default void existsByIdOrElseThrow(ObjectId relatedPostId) {
        if (!existsById(relatedPostId)) throw new NotFoundException("Could not find any post with id " + relatedPostId);
    }
}
