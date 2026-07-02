package it.itsacademy.springoauth2resourceserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
// TODO implementare auditing
public class UserProfile {
    @Id private UUID idProfile;
    @ManyToOne @JoinColumn(name = "user_id", nullable = false) private User user;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String surname;
    private String avatarUrl;
    private String biografia;
}
