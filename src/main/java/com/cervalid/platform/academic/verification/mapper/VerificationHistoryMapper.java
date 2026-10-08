package com.cervalid.platform.academic.verification.mapper;

import com.cervalid.platform.academic.verification.dto.response.VerificationHistoryResponse;
import com.cervalid.platform.academic.verification.dto.view.VerificationHistoryView;
import org.springframework.stereotype.Component;

@Component
public class VerificationHistoryMapper {

    public VerificationHistoryResponse toResponse(
            VerificationHistoryView view) {

        //certificate = record.getCertificate();

        return VerificationHistoryResponse.builder()
                .publicId(view.getPublicId())
                .verificationType(view.getVerificationType())
                .resourcePublicId(view.getResourcePublicId())
                .resourceNumber(view.getResourceNumber())
                .resourceType(view.getResourceType())
                .studentName(view.getStudentName())
                .institutionName(view.getInstitutionName())
                .verificationReason(view.getVerificationReason())
                .status(view.getStatus())
                .verificationSource(view.getVerificationSource())
                .ip(view.getIp())
                .verifiedAt(view.getVerifiedAt())
                .build();
    }
}
