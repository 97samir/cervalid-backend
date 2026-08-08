package com.cervalid.platform.academic.verification.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VerificationHistoryResponse {

    private UUID publicId;
    private String certificateNumber;
    private UUID certificatePublicId;

    private String studentName;
    private String institutionName;
    private String certificateType;
    private String verificationReason;

    private String status;
    private String verificationSource;
    private String ip;
    private LocalDateTime verifiedAt;


}