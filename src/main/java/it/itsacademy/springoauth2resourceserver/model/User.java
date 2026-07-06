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
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID userId;
    @Column(nullable = false, unique = true) private String sub;
    @Column(nullable = false, unique = true) private String email;
    // NB: se si usano altri issuer andrebbe aggiunta una colonna "issuer" e sub (ed email) non può esser unique
    @NotEmpty @ElementCollection @Enumerated(EnumType.STRING) private Set<Role> roles = Set.of(Role.USER);
    @Column(nullable = false) private boolean isActive = true;

    public enum Role {
        ADMIN, MANAGER, USER
    }
}
