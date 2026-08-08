package com.cervalid.platform.auth.token.entity;

import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "activation_tokens")
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean used = false;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
