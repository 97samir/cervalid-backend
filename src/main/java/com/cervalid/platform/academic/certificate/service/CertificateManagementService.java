package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.dto.request.IssueCertificateRequest;
import com.cervalid.platform.academic.certificate.dto.request.UpdateCertificateCredentialRequest;
import com.cervalid.platform.academic.certificate.dto.response.CertificateDocumentResponse;
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
import org.springframework.web.multipart.MultipartFile;

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

    private final CertificateDocumentValidationService documentValidationService;
    private final CertificateHashService certificateHashService;

    public CertificateResponse issue(
            IssueCertificateRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Transcript transcript =
                transcriptValidator.validateOwnership(
                        request.getTranscriptPublicId(),
                        institutionId
                );

        validationService.validateTranscriptForIssuance(
                transcript);

        documentValidationService.validateOptionalDocument(
                request.getDocumentHash(),
                request.getDocumentUrl()
        );

        Certificate certificate =
                builderService.build(
                        transcript,
                        request.getType(),
                        request.getTitle(),
                        request.getAwardedAt(),
                        request.getDocumentHash(),
                        request.getDocumentUrl()
                );

        Certificate saved = generationService.generate(
                certificate,
                transcript);

        timelineService.issued(saved);

        return mapper.toResponse(saved);
    }

    public CertificateResponse issueFromTranscript(
            UUID transcriptPublicId,
            IssueCertificateRequest request) {

        return issue(
                IssueCertificateRequest.builder()
                        .transcriptPublicId(transcriptPublicId)

                        .type(request != null
                                && request.getType() != null
                                ? request.getType()
                                : CertificateType.DEGREE)

                        .title(request != null
                                ? request.getTitle()
                                : null)

                        .awardedAt(request != null
                                ? request.getAwardedAt()
                                : null)

                        .documentHash(request != null
                                ? request.getDocumentHash()
                                : null)

                        .documentUrl(request != null
                                ? request.getDocumentUrl()
                                : null)

                        .build()
        );
    }

    public CertificateResponse updateCredential(
            UUID publicId,
            UpdateCertificateCredentialRequest request) {

        Certificate certificate =
                validationService.getCertificate(publicId);

        validationService.validateCanUpdateCredential(
                certificate);

        validateCredentialRequest(request);

        certificate.setTitle(request.getTitle().trim());
        certificate.setAwardedAt(request.getAwardedAt());
        certificateHashService.generateHashes(certificate);

        Certificate saved = repository.save(certificate);

        timelineService.credentialUpdated(saved);

        return mapper.toResponse(saved);
    }

    public CertificateDocumentResponse updateDocument(
            UUID publicId,
            MultipartFile file,
            String documentHash,
            String documentUrl) {

        Certificate certificate =
                validationService.getCertificate(publicId);

        if (certificate.getStatus()
                != CertificateStatus.ISSUED) {

            throw new IllegalStateException(
                    "Solo se puede asociar un documento a un certificado emitido."
            );
        }

        String finalDocumentHash =
                documentValidationService.validateAndResolveHash(
                        file,
                        documentHash,
                        documentUrl
                );

        certificate.setDocumentHash(finalDocumentHash);

        if (documentUrl != null
                && !documentUrl.isBlank()) {

            certificate.setDocumentUrl(
                    documentUrl.trim()
            );
        }

        Certificate saved =
                repository.save(certificate);

        return CertificateDocumentResponse.builder()
                .certificatePublicId(saved.getPublicId().toString())
                .documentHash(saved.getDocumentHash())
                .documentUrl(saved.getDocumentUrl())
                .build();
    }

    public void revoke(UUID publicId) {

        Certificate certificate =
                validationService.getCertificate(publicId);

        validationService.validateCanRevoke(certificate);
        certificate.setStatus(CertificateStatus.REVOKED);
        certificate.setRevokedAt(LocalDateTime.now());

        repository.save(certificate);
        timelineService.revoked(certificate);
    }

    private void validateCredentialRequest(
            UpdateCertificateCredentialRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Los datos de la credencial son obligatorios."
            );
        }

        if (request.getTitle() == null
                || request.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                    "El título de la credencial es obligatorio."
            );
        }

        if (request.getAwardedAt() == null) {
            throw new IllegalArgumentException(
                    "La fecha de otorgamiento es obligatoria."
            );
        }
    }
}