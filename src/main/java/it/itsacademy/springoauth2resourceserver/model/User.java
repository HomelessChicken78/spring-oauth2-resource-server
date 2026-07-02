package it.itsacademy.springoauth2resourceserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
@Entity
public class User {
    @Id private UUID userId;
    @Column(nullable = false) private String sub;
    @Column(nullable = false) private String email;
    @NotEmpty @ElementCollection @Enumerated(EnumType.STRING) private Set<Role> roles;
    @Column(nullable = false) private boolean isActive;

    public enum Role {
        ADMIN, MANAGER, USER
    }
}
