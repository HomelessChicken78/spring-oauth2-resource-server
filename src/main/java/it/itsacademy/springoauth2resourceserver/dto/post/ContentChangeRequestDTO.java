package it.itsacademy.springoauth2resourceserver.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class ContentChangeRequestDTO {
    @NotNull(message = "Content for post is mandatory.")
    private String content;
}
