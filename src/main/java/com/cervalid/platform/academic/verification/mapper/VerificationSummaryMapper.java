package com.cervalid.platform.academic.verification.mapper;

import com.cervalid.platform.academic.verification.dto.response.VerificationSummaryResponse;
import com.cervalid.platform.academic.verification.dto.view.VerificationSummaryView;
import org.springframework.stereotype.Component;

@Component
public class VerificationSummaryMapper {

    public VerificationSummaryResponse toResponse(
            VerificationSummaryView view) {

        return VerificationSummaryResponse.builder()

                .resourcePublicId(view.getResourcePublicId()
                )
                .verificationType(view.getVerificationType())
                .resourceNumber(view.getResourceNumber())
                .resourceType(view.getResourceType())
                .studentName(view.getStudentName())
                .institutionName(view.getInstitutionName())
                .verificationCount(view.getVerificationCount())
                .lastVerifiedAt(view.getLastVerifiedAt())
                .lastStatus(view.getLastStatus())
                .build();
    }
}