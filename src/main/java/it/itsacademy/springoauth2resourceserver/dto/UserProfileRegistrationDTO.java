package it.itsacademy.springoauth2resourceserver.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class UserProfileRegistrationDTO {
    @Pattern(
            regexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)",
            message = "Invalid URL"
    )
    private String avatarUrl;
}
