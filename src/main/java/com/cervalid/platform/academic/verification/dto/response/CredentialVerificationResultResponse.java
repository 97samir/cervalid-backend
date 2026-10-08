package com.cervalid.platform.academic.verification.dto.response;

import com.cervalid.platform.academic.credential.dto.response.CredentialDocumentResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialVerificationResponse;
import com.cervalid.platform.academic.verification.enums.VerificationLevel;
import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialVerificationResultResponse {

    private VerificationStatus status;
    private VerificationLevel verificationLevel;
    private CredentialVerificationResponse credential;
    private CredentialDocumentResponse document;
}