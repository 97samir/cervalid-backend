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
public class CredentialVerificationResponse {

    private UUID credentialPublicId;
    private String credentialNumber;

    private CredentialType type;
    private String title;
    private String description;

    private LocalDate awardedAt;
    private LocalDateTime issuedAt;
    private CredentialStatus status;

    private String verificationUrl;

    private CredentialRecipientResponse recipient;
    private CredentialIssuerResponse issuer;
    private CredentialAcademicResponse academic;
    private CredentialDocumentResponse document;

    private CredentialIntegrityResponse integrity;
    private CredentialRevocationResponse revocation;
}