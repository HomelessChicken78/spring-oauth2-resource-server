package it.itsacademy.springoauth2resourceserver.dto.post;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class TitleChangeRequestDTO {
    @NotBlank(message = "Title for post is mandatory.")
    @Size(max = 50, message = "Title must be at most 50 characters.")
    @Pattern(regexp = "[\\w!?\" ':,.-]*", message = "Invalid character(s) for title.")
    private String title;
}
