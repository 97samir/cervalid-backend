package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.dto.request.IssueCertificateRequest;
import com.cervalid.platform.academic.certificate.dto.response.CertificateResponse;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.enums.CertificateType;
import com.cervalid.platform.academic.certificate.mapper.CertificateMapper;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.shared.validation.TranscriptOwnershipValidator;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateManagementService {

    private final CertificateRepository repository;
    private final SecurityContextService securityContextService;
    private final TranscriptOwnershipValidator transcriptValidator;
    private final CertificateValidationService validationService;
    private final CertificateBuilderService builderService;
    private final CertificateGenerationService generationService;
    private final CertificateTimelineService timelineService;
    private final CertificateMapper mapper;

    public CertificateResponse issue(
            IssueCertificateRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Transcript transcript =
                transcriptValidator.validateOwnership(
                        request.getTranscriptPublicId(),
                        institutionId);

        validationService.validateTranscriptForIssuance(
                transcript);

        Certificate certificate = builderService.build(
                transcript,
                request.getType()
        );

        Certificate saved = generationService.generate(
                certificate,
                transcript
        );

        timelineService.issued(saved);

        return mapper.toResponse(saved);
    }

    public CertificateResponse issueFromTranscript(
            UUID transcriptPublicId) {

        return issue(
                IssueCertificateRequest.builder()
                        .transcriptPublicId(transcriptPublicId)
                        .type(CertificateType.DEGREE)
                        .build()
        );
    }

    public void revoke(
            UUID publicId) {

        Certificate certificate =
                validationService.getCertificate(publicId);

        validationService.validateCanRevoke(certificate);
        certificate.setStatus(CertificateStatus.REVOKED);
        certificate.setRevokedAt(LocalDateTime.now());
        repository.save(certificate);
        timelineService.revoked(certificate);
    }

}