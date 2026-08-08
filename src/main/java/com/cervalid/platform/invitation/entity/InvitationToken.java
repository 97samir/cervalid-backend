/*package com.cervalid.platform.invitation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "invitation_tokens")
public class InvitationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = false)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private UUID institutionId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean used = false;

}
*/