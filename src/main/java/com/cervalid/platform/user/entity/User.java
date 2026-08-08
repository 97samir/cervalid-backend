package com.cervalid.platform.user.entity;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.institution.entity.Institution;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @Column(nullable = false, unique = true, length = 9)
    private String document;

    @Pattern(regexp = "\\d{9}", message = "Teléfono inválido")
    @Column(nullable = false, length = 9)
    private String phone;

    @Enumerated(EnumType.STRING)
    private RoleName globalRole;

    @Column(nullable = false)
    private boolean active = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
