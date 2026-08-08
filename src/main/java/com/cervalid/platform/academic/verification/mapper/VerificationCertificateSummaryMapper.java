package com.cervalid.platform.academic.verification.mapper;

import com.cervalid.platform.academic.verification.dto.response.VerificationCertificateSummaryResponse;
import com.cervalid.platform.academic.verification.dto.view.VerificationCertificateSummaryView;
import org.springframework.stereotype.Component;

@Component
public class VerificationCertificateSummaryMapper {

    public VerificationCertificateSummaryResponse toResponse(
            VerificationCertificateSummaryView view) {

        return VerificationCertificateSummaryResponse.builder()

                .certificatePublicId(view.getCertificatePublicId())
                .certificateNumber(view.getCertificateNumber())
                .studentName(view.getStudentName())
                .institutionName(view.getInstitutionName())
                .certificateType(view.getCertificateType())
                .verificationCount(view.getVerificationCount())
                .lastVerifiedAt(view.getLastVerifiedAt())
                .lastStatus(view.getLastStatus())

                .build();
    }

}