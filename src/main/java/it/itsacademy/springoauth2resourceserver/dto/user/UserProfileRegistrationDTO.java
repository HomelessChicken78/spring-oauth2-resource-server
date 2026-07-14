package it.itsacademy.springoauth2resourceserver.dto.user;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Nickname is mandatory")
    @Pattern(
            regexp = "^\\w{1,20}$",
            message = "Nickname must contain only letters, numbers, or underscores, and be 1–20 characters long."
    )
    private String nickname;
}
