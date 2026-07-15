package it.itsacademy.springoauth2resourceserver.dto.post.comment;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data
public class CommentResponseDTO {
    private String id, content;
    private LocalDateTime createdAt, updatedAt;
    private String author;
}
