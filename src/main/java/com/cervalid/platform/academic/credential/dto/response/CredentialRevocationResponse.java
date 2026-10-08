package com.cervalid.platform.academic.credential.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialRevocationResponse {

    private LocalDateTime revokedAt;
    private String reason;
}