package com.cervalid.platform.academic.verification.dto.view;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VerificationHistoryView {

    private UUID publicId;
    private UUID certificatePublicId;
    private String certificateNumber;
    private String certificateType;
    private String studentName;
    private String institutionName;
    private String verificationReason;
    private String status;
    private String verificationSource;
    private String ip;
    private LocalDateTime verifiedAt;
}