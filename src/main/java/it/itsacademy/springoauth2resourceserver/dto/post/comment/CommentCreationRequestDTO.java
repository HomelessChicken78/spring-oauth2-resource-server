package it.itsacademy.springoauth2resourceserver.dto.post.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class CommentCreationRequestDTO {
    @NotBlank(message = "Content cannot be blank")
    private String content;
}
