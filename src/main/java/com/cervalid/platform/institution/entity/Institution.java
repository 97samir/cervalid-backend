package com.cervalid.platform.institution.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "institutions")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean tienePresenciaDigital;

    @Column(nullable = true)
    private String website;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true, unique = true)
    private String institutionalEmail;

    @Column(nullable = true)
    private String institutionalDominio;

    @Column(nullable = false)
    private Boolean active = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "wallet_address", unique = true)
    private String walletAddress;
}
