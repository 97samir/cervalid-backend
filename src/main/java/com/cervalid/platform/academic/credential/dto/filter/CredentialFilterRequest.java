package com.cervalid.platform.academic.credential.dto.filter;

import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialFilterRequest {

    private UUID studentPublicId;
    private CredentialType type;
    private CredentialStatus status;
    private String credentialNumber;
    private String title;
}