package com.cervalid.platform.academic.verification.dto.request;

import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class VerificationHistoryFilterRequest {

    private String verificationType;
    private UUID resourcePublicId;
    private VerificationStatus status;
    private String search;
    private LocalDate fromDate;
    private LocalDate toDate;
}
