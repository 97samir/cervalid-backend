package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.verification.dto.view.VerificationCertificateSummaryView;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationSummaryQueryService {

    private final VerificationRecordRepository verificationRecordRepository;
    private final VerificationQueryService verificationQueryService;

    public VerificationCertificateSummaryView build(
            Certificate certificate) {

        VerificationRecord lastVerification =
                verificationRecordRepository
                        .findFirstByCertificateIdOrderByVerifiedAtDesc(
                                certificate.getId());

        long total =
                verificationRecordRepository.countByCertificateId(
                        certificate.getId());

        var verificationView =
                verificationQueryService.buildVerificationView(
                        certificate.getStudentId(),
                        certificate.getInstitutionId());

        return VerificationCertificateSummaryView.builder()

                .certificatePublicId(certificate.getPublicId())
                .certificateNumber(certificate.getCertificateNumber())
                .studentName(verificationView.getStudentName())
                .institutionName(verificationView.getInstitutionName())
                .certificateType(certificate.getType().name())
                .verificationCount(total)
                .lastVerifiedAt(lastVerification != null
                        ? lastVerification.getVerifiedAt()
                        : null)
                .lastStatus(lastVerification != null
                        ? lastVerification.getStatus()
                        : null)
                .build();
    }

}