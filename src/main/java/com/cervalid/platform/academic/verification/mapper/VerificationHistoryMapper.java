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
                .certificatePublicId(view.getPublicId())
                .certificateNumber(view.getCertificateNumber())
                .studentName(view.getStudentName())
                .institutionName(view.getInstitutionName())
                .certificateType(view.getCertificateType())
                .verificationReason(view.getVerificationReason())
                .status(view.getStatus())
                .verificationSource(view.getVerificationSource())
                .ip(view.getIp())
                .verifiedAt(view.getVerifiedAt())
                .build();
    }
}
