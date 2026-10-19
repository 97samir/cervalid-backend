package com.cervalid.platform.user.entity;

import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.security.permissions.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="institution_users",
        uniqueConstraints=@UniqueConstraint(
                columnNames={"user_id","institution_id"}
        ))
public class InstitutionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID publicId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="institution_id")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

    @Column(nullable = false)
    private boolean active = true;

    @PrePersist
    public void prePersist() {

        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }
}