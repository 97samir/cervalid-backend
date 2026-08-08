package com.cervalid.platform.academic.verification.dto.request;

import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class VerificationHistoryFilterRequest {

    private UUID certificatePublicId;
    private VerificationStatus status;
    private String search; // numero de certificado
    private LocalDate fromDate;
    private LocalDate toDate;
}
