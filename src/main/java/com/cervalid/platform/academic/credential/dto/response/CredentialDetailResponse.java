package com.cervalid.platform.academic.credential.dto.response;

import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDetailResponse {

    private UUID publicId;
    private UUID studentPublicId;
    private String credentialNumber;

    private CredentialType type;
    private String title;
    private String description;
    private LocalDate awardedAt;
    private LocalDateTime issuedAt;
    private CredentialStatus status;

    private String documentHash;
    private String documentUrl;

    private String blockchainTxHash;
    private String blockchainNetwork;
    private Long blockNumber;

    private LocalDateTime anchoredAt;
    private LocalDateTime revokedAt;

    private String revocationReason;
}