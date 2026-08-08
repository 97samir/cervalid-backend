package com.cervalid.platform.invitation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class InvitationResponse {
    private String email;
    private String role;
    private boolean used;
    private LocalDateTime expiresAt;
}