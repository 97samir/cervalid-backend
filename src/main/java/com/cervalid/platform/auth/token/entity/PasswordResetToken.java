package com.cervalid.platform.auth.token.entity;

import com.cervalid.platform.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean used = false;

}
