package com.cervalid.platform.academic.verification.dto.response;

import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VerificationCertificateSummaryResponse {

    private UUID certificatePublicId;
    private String certificateNumber;
    private String studentName;
    private String institutionName;
    private String certificateType;
    private long verificationCount;
    private LocalDateTime lastVerifiedAt;
    private VerificationStatus lastStatus;
}