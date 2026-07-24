package it.itsacademy.springoauth2resourceserver.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Audited;
import org.hibernate.envers.AuditOverride;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Audited @AuditOverride(forClass = Auditable.class)
public class UserProfile extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID idProfile;
    @ManyToOne @JoinColumn(name = "user_id", nullable = false) private User user;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String surname;
    @Column(nullable = false, unique = true) private String nickname;
    private String avatarUrl;
    private String biografia;
    private Integer followers;
    private Integer followings;
}
