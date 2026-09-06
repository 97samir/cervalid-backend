package com.cervalid.platform.academic.verification.dto.request;

import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class VerificationCertificateSummaryFilterRequest {

    private String search;
    private VerificationStatus status;
    private String type;
    private LocalDate fromDate;
    private LocalDate toDate;
}