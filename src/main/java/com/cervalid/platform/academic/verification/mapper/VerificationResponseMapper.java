package com.cervalid.platform.academic.verification.mapper;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.timeline.dto.view.PublicTimelineEventView;
import com.cervalid.platform.academic.verification.dto.response.VerifyCertificateResponse;
import com.cervalid.platform.academic.verification.dto.view.PublicCertificateView;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.enums.VerificationLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VerificationResponseMapper {

    public VerifyCertificateResponse toResponse(
            Certificate certificate,
            VerificationRecord record,
            PublicCertificateView view,
            List<PublicTimelineEventView> timeline,
            String message,
            VerificationLevel verificationLevel) {

        VerifyCertificateResponse response =
                new VerifyCertificateResponse();

        response.setValid(record.isValid());
        response.setVerificationStatus(record.getStatus().name());
        response.setVerificationMessage(message);
        response.setVerificationLevel(verificationLevel);

        response.setTimeline(timeline);

        if (certificate != null) {

            response.setCertificateNumber(certificate.getCertificateNumber());
            response.setCertificatePublicId(certificate.getPublicId());

            if (certificate.getIssuedAt() != null) {
                response.setIssuedAt(certificate.getIssuedAt().toString());
            }

            if (certificate.getAwardedAt() != null) {
                response.setAwardedAt(certificate.getAwardedAt().toString());
            }

            if (certificate.getType() != null) {
                response.setCertificateType(certificate.getType().name());
            }

            response.setDocumentHash(certificate.getDocumentHash());
            response.setDocumentUrl(certificate.getDocumentUrl());

            if (view != null) {
                response.setStudentName(view.getStudentName());
                response.setInstitutionName(view.getInstitutionName());
                response.setProgram(view.getProgram());
                response.setFaculty(view.getFaculty());
                response.setModality(view.getModality());
                response.setCurrentCycle(view.getCurrentCycle());
            }
        }

        return response;
    }

    /*
    private VerificationLevel resolveLevel(
            Certificate certificate,
            VerificationRecord record) {

        if (certificate == null) {
            return VerificationLevel.LOCAL;
        }

        if (certificate.getStatus() ==
                CertificateStatus.REVOKED) {

            return VerificationLevel.LOCAL;
        }

        if (!record.isValid()) {
            return VerificationLevel.LOCAL;
        }

        if (certificate.getBlockchainTxHash() != null &&
                !certificate.getBlockchainTxHash().isBlank()) {

            return VerificationLevel.BLOCKCHAIN_VALIDATED;
        }

        return VerificationLevel.HASH_VALIDATED;
    }
     */
}