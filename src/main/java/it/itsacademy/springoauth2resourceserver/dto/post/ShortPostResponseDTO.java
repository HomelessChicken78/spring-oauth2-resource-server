package it.itsacademy.springoauth2resourceserver.dto.post;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShortPostResponseDTO {
    @EqualsAndHashCode.Include private String id;
    private String title, author;
}
