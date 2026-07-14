package it.itsacademy.springoauth2resourceserver.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comment")
@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {
    @Id @EqualsAndHashCode.Include private ObjectId id;
    @Indexed private ObjectId postId; // @Indexed Creates a database index to speed up queries fetching comments by post

    private String content;

    @CreatedDate private LocalDateTime createdAt;

    @LastModifiedDate private LocalDateTime updatedAt;
}
