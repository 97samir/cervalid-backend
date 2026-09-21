package com.cervalid.platform.academic.credential.dto.filter;

import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialFilterRequest {

    private Long studentId;
    private CredentialType type;
    private CredentialStatus status;
    private String credentialNumber;
    private String title;
}