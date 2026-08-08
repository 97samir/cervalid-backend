package com.cervalid.platform.academic.verification.dto.view;

import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VerificationCertificateSummaryView {

    private UUID certificatePublicId;
    private String certificateNumber;
    private String studentName;
    private String institutionName;
    private String certificateType;
    private long verificationCount;
    private LocalDateTime lastVerifiedAt;
    private VerificationStatus lastStatus;
}