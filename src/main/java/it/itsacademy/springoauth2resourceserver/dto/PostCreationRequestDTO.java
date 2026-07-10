package it.itsacademy.springoauth2resourceserver.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter @ToString
public class PostCreationRequestDTO {
    @NotBlank(message = "Title for post is mandatory.")
    @Size(max = 50, message = "Title must be at most 50 characters.")
    @Pattern(regexp = "[\\w!?\" '-]*", message = "Invalid character(s) for title.")
    private String title;

    private String content;

    @Size(min = 1, max = 50, message = "Topic name must be at least 1 character and at most 50 characters.")
    @Pattern(regexp = "[\\w!?\" '-]*", message = "Invalid character(s) for topic name.")
    private String topic;

    @Size(max = 7, message = "There can't be more than 7 related posts.")
    @ToString.Exclude private Set<String> relatedPosts = new HashSet<>();
}
