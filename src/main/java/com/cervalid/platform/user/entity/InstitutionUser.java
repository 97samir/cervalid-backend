package com.cervalid.platform.user.entity;

import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.security.permissions.Role;
import jakarta.persistence.*;
import lombok.*;

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
}