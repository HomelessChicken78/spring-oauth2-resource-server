package it.itsacademy.springoauth2resourceserver.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@MappedSuperclass @EntityListeners(AuditingEntityListener.class)
public class Auditable {
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    @LastModifiedBy private String updatedBy;
}