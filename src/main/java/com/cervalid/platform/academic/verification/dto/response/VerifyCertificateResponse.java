package com.cervalid.platform.academic.verification.dto.response;

import com.cervalid.platform.academic.verification.enums.VerificationLevel;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCertificateResponse {

    private boolean valid;
    private String verificationStatus;
    private String certificateNumber;
    private String studentName;
    private String institutionName;

    private String program;
    private String faculty;
    private String modality;
    private Integer currentCycle;
    private String certificateType;

    private String issuedAt;
    private String verificationMessage;
    private VerificationLevel verificationLevel;

}
