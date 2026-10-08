package com.cervalid.platform.academic.verification.dto.view;

import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VerificationSummaryView {

    private UUID resourcePublicId;
    private String verificationType;

    private String resourceNumber;
    private String resourceType;

    private String studentName;
    private String institutionName;

    private long verificationCount;

    private LocalDateTime lastVerifiedAt;
    private VerificationStatus lastStatus;
}