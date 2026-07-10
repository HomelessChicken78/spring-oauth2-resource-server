package it.itsacademy.springoauth2resourceserver.dto;

import lombok.*;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Builder
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostResponseDTO {
    @EqualsAndHashCode.Include private String id;
    private String title, content, topic, author;
    @ToString.Exclude private Set<ShortPostResponseDTO> relatedPosts;
}
