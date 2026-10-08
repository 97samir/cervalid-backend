package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.verification.dto.view.VerificationSummaryView;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationSummaryQueryService {

    private final VerificationRecordRepository verificationRecordRepository;
    private final VerificationQueryService verificationQueryService;

    public VerificationSummaryView buildCertificate(
            Certificate certificate) {

        VerificationRecord lastVerification =
                verificationRecordRepository
                        .findFirstByCertificateIdOrderByVerifiedAtDesc(
                                certificate.getId()
                        );

        long total =
                verificationRecordRepository
                        .countByCertificateId(
                                certificate.getId()
                        );

        var verificationView =
                verificationQueryService.buildVerificationView(
                        certificate.getStudentId(),
                        certificate.getInstitutionId()
                );

        return VerificationSummaryView.builder()

                .resourcePublicId(certificate.getPublicId())
                .verificationType("CERTIFICATE")
                .resourceNumber(certificate.getCertificateNumber())
                .resourceType(certificate.getType()
                        != null
                        ? certificate.getType().name()
                        : null)
                .studentName(verificationView.getStudentName())
                .institutionName(verificationView.getInstitutionName())
                .verificationCount(total)
                .lastVerifiedAt(lastVerification
                        != null
                        ? lastVerification.getVerifiedAt()
                        : null)
                .lastStatus(lastVerification
                        != null
                        ? lastVerification.getStatus()
                        : null)
                .build();
    }

    public VerificationSummaryView buildCredential(
            Credential credential) {

        VerificationRecord lastVerification =
                verificationRecordRepository
                        .findFirstByCredentialIdOrderByVerifiedAtDesc(
                                credential.getId()
                        );

        long total =
                verificationRecordRepository
                        .countByCredentialId(
                                credential.getId()
                        );

        String studentName =
                verificationQueryService.resolveStudentName(
                        credential.getStudentId()
                );

        String institutionName =
                verificationQueryService.resolveInstitutionName(
                        credential.getInstitutionId()
                );

        return VerificationSummaryView.builder()

                .resourcePublicId(credential.getPublicId())
                .verificationType("CREDENTIAL")
                .resourceNumber(credential.getCredentialNumber())
                .resourceType(credential.getType()
                        != null
                        ? credential.getType().name()
                        : null)
                .studentName(studentName)
                .institutionName(institutionName)
                .verificationCount(total)
                .lastVerifiedAt(lastVerification
                        != null
                        ? lastVerification.getVerifiedAt()
                        : null)
                .lastStatus(lastVerification
                        != null
                        ? lastVerification.getStatus()
                        : null)
                .build();
    }
}