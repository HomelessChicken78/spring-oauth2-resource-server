package it.itsacademy.springoauth2resourceserver.dto.user;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class UserProfileResponseDTO {
    private String name, surname, nickname, avatarUrl, biografia;
}
