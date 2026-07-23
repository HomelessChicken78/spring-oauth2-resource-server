package it.itsacademy.springoauth2resourceserver.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"follower_id", "following_id"}
        ),
        check = @CheckConstraint(
                name = "chk_follower_diff_following",
                constraint = "follower_id <> following_id"
        )
)
@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Audited(targetAuditMode = NOT_AUDITED) @EntityListeners(AuditingEntityListener.class)
public class Follow {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID followId;

    @ManyToOne @JoinColumn(name = "follower_id")
    private UserProfile follower;

    @ManyToOne @JoinColumn(name = "following_id")
    private UserProfile following;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate private LocalDateTime createdAt;
}
