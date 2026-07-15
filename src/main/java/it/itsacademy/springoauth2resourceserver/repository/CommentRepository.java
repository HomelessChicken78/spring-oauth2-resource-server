package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.exception.NotFoundException;
import it.itsacademy.springoauth2resourceserver.model.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    Page<Comment> findByPostId(ObjectId postId, Pageable page);

    default Comment findByPostIdOrElseThrow(ObjectId commentId, ObjectId postId) {
        return findById(commentId)
                .orElseThrow(() -> new NotFoundException("Could not find any comment with id " + commentId + " of post " + postId));
    }
}
