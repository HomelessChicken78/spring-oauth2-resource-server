package it.itsacademy.springoauth2resourceserver.dto.post;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Builder
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostResponseDTO {
    @EqualsAndHashCode.Include private String id;
    private String title, content, topic, author;
    @ToString.Exclude private Set<ShortPostResponseDTO> relatedPosts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
