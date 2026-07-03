package it.itsacademy.springoauth2resourceserver.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class UserProfileRegistrationDTO {
    @NotEmpty
    private String avatarUrl;
}
