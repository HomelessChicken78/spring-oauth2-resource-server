package it.itsacademy.springoauth2resourceserver.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "post")
@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {
    @Id @EqualsAndHashCode.Include private ObjectId id;
    private String title, content, topic, author;

    @Size(max = 7, message = "There can't be more than 7 related posts")
    @Builder.Default @ToString.Exclude
    private Set<ObjectId> relatedPosts = new HashSet<>();
}
