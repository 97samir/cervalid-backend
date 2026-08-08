package com.cervalid.platform.tenant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre visible de la institución
    @Column(nullable = false)
    private String name;

    // URL del logo institucional
    private String logoUrl;

    // Color principal (white-label SaaS)
    private String primaryColor;

    // Estado del tenant (activo/inactivo)
    @Column(nullable = false)
    private Boolean active = true;
}
