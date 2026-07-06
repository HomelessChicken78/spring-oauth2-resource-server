package it.itsacademy.springoauth2resourceserver.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data
public class UserProfileResponseDTO {
    private String name, surname, avatarUrl, biografia;
}
