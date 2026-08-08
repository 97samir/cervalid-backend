package com.cervalid.platform.security.permissions;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "role_permissions")

public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Role role;

    @ManyToOne(optional = false)
    private Permission permission;

    // Constructor vacío (OBLIGATORIO para JPA) - acceder internamente
    public RolePermission() {
    }

    // Constructor de conveniencia
    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }
}
