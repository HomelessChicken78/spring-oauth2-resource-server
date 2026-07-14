package it.itsacademy.springoauth2resourceserver.repository;

import it.itsacademy.springoauth2resourceserver.model.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByPostId(ObjectId postId, Pageable page);
}
