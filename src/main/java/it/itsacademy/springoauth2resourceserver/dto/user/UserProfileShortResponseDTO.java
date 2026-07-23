package it.itsacademy.springoauth2resourceserver.dto.user;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Data
public class UserProfileShortResponseDTO {
    private UUID idProfile;
    private String nickname, avatarUrl;
}
